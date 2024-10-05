package io.github.sefiraat.networks;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.enums.MCVersion;
import com.ytdd9527.networksexpansion.core.managers.ConfigManager;
import com.ytdd9527.networksexpansion.implementation.guide.CheatGuideImpl;
import com.ytdd9527.networksexpansion.implementation.guide.SurvivalGuideImpl;
import com.ytdd9527.networksexpansion.setup.SetupUtil;
import com.ytdd9527.networksexpansion.utils.databases.DataSource;
import com.ytdd9527.networksexpansion.utils.databases.DataStorage;
import com.ytdd9527.networksexpansion.utils.databases.QueryQueue;
import io.github.sefiraat.networks.commands.NetworksMain;
import io.github.sefiraat.networks.integrations.HudCallbacks;
import io.github.sefiraat.networks.integrations.NetheoPlants;
import io.github.sefiraat.networks.managers.ListenerManager;
import io.github.sefiraat.networks.managers.SupportedPluginManager;
import io.github.sefiraat.networks.slimefun.NetworksSlimefunItemStacks;
import io.github.sefiraat.networks.slimefun.network.NetworkController;
import io.github.sefiraat.networks.utils.NetworkUtils;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Networks extends JavaPlugin implements SlimefunAddon {
    private static Networks instance;
    private static DataSource dataSource;
    private static QueryQueue queryQueue;
    private static BukkitRunnable autoSaveThread;
    private final String username;
    private final String repo;
    private final String branch;
    private ConfigManager configManager;
    private ListenerManager listenerManager;
    private SupportedPluginManager supportedPluginManager;
    private long slimefunTickCount;


    public Networks() {
        this.username = "ytdd9527";
        this.repo = "NetworksExpansion";
        this.branch = "master";
    }

    public static ConfigManager getConfigManager() {
        return Networks.getInstance().configManager;
    }

    public static QueryQueue getQueryQueue() {
        return queryQueue;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static BukkitRunnable getAutoSaveThread() {
        return autoSaveThread;
    }

    public static Networks getInstance() {
        return Networks.instance;
    }

    @Nonnull
    public static PluginManager getPluginManager() {
        return Networks.getInstance().getServer().getPluginManager();
    }

    public static SupportedPluginManager getSupportedPluginManager() {
        return Networks.getInstance().supportedPluginManager;
    }

    public static ListenerManager getListenerManager() {
        return Networks.getInstance().listenerManager;
    }

    public static long getSlimefunTickCount() {
        return getInstance().slimefunTickCount;
    }

    @Override
    public void onEnable() {
        instance = this;

        superHead();
        getLogger().info("Obtaining configuration...");
        saveDefaultConfig();

        getLogger().info("Trying to automatically update...");
        this.configManager = new ConfigManager();

        this.supportedPluginManager = new SupportedPluginManager();

        // Try connect database
        getLogger().info("Connecting to database files...");
        try {
            dataSource = new DataSource();
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().warning("Database files connection failed!");
            e.printStackTrace();
            onDisable();
        }

        getLogger().info("Queuing...");
        queryQueue = new QueryQueue();
        queryQueue.startThread();

        getLogger().info("Creating auto save threads...");
        autoSaveThread = new BukkitRunnable() {
            @Override
            public void run() {
                DataStorage.saveAmountChange();
            }
        };
        // 5m * 60s * 20 ticks
        long period = 5 * 60 * 20;
        autoSaveThread.runTaskTimerAsynchronously(this, 2 * period, period);

        getLogger().info("Registering items...");
        SetupUtil.setupAll();

        getLogger().info("Registering instruction...");
        this.listenerManager = new ListenerManager();
        this.getCommand("networks").setExecutor(new NetworksMain());

        setupMetrics();

        Bukkit.getScheduler()
                .runTaskTimer(
                        this,
                        () -> slimefunTickCount++,
                        1,
                        Slimefun.getTickerTask().getTickRate());

        // Fix dupe bug which break the network controller data without player interaction
        Bukkit.getScheduler()
                .runTaskTimer(
                        this,
                        () -> {
                            for (Location controller : NetworkController.getNetworks().keySet()) {
                                SlimefunBlockData data = StorageCacheUtils.getBlock(controller);
                                if (data == null || !NetworksSlimefunItemStacks.NETWORK_CONTROLLER.getItemId().equals(data.getSfId())) {
                                    NetworkUtils.clearNetwork(controller);
                                }
                            }
                        },
                        1,
                        Slimefun.getTickerTask().getTickRate());

        final boolean survivalOverride = getConfig().getBoolean("integrations.guide.survival-override");
        final boolean cheatOverride = getConfig().getBoolean("integrations.guide.cheat-override");
        if (survivalOverride || cheatOverride) {
            getLogger().info("检测到已开启指南替换功能");
            getLogger().info("正在替换指南...");
            Field field = ReflectionUtil.getField(Slimefun.getRegistry().getClass(), "guides");
            if (field != null) {
                field.setAccessible(true);

                Map<SlimefunGuideMode, SlimefunGuideImplementation> newGuides = new EnumMap<>(SlimefunGuideMode.class);
                newGuides.put(SlimefunGuideMode.SURVIVAL_MODE, survivalOverride ? new SurvivalGuideImpl() : new SurvivalSlimefunGuide());
                newGuides.put(SlimefunGuideMode.CHEAT_MODE, cheatOverride ? new CheatGuideImpl() : new CheatSheetSlimefunGuide());
                try {
                    field.set(Slimefun.getRegistry(), newGuides);
                } catch (IllegalAccessException ignored) {

                }
            }
            getLogger().info(survivalOverride ? "已开启替换生存指南!" : "未关闭替换生存指南!");
            getLogger().info(cheatOverride ? "已开启替换作弊指南!" : "未关闭替换作弊指南!");
            getLogger().info("如遇开启后其他插件报错, 请在配置文件(config.yml)中关闭此功能");
        }


        getLogger().info("已启用附属！");
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving configuration...");
        this.configManager.saveAll();
        getLogger().info("Saving database, do not end the process!");
        if (autoSaveThread != null) {
            autoSaveThread.cancel();
        }
        DataStorage.saveAmountChange();
        if (queryQueue != null) {
            while (!queryQueue.isAllDone()) {
                getLogger().info("Active queue: " + queryQueue.getTaskAmount());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queryQueue.scheduleAbort();
        }
        getLogger().info("Database saved!");
        getLogger().info("Closing tasks...");
        getLogger().info("Tasks closed!");
        getLogger().info("Dependencies safely disabled!");
    }

    public void superHead() {
        getLogger().info("#########################################################################");
        getLogger().info("███╗   ██╗███████╗████████╗██╗    ██╗ ██████╗ ██████╗ ██╗  ██╗           ");
        getLogger().info("████╗  ██║██╔════╝╚══██╔══╝██║    ██║██╔═══██╗██╔══██╗██║ ██╔╝           ");
        getLogger().info("██╔██╗ ██║█████╗     ██║   ██║ █╗ ██║██║   ██║██████╔╝█████╔╝            ");
        getLogger().info("██║╚██╗██║██╔══╝     ██║   ██║███╗██║██║   ██║██╔══██╗██╔═██╗            ");
        getLogger().info("██║ ╚████║███████╗   ██║   ╚███╔███╔╝╚██████╔╝██║  ██║██║  ██╗           ");
        getLogger().info("╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚══╝╚══╝  ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝           ");
        getLogger().info("███████╗██╗  ██╗██████╗  █████╗ ███╗   ██╗███████╗██╗ ██████╗ ███╗   ██╗ ");
        getLogger().info("██╔════╝╚██╗██╔╝██╔══██╗██╔══██╗████╗  ██║██╔════╝██║██╔═══██╗████╗  ██║ ");
        getLogger().info("█████╗   ╚███╔╝ ██████╔╝███████║██╔██╗ ██║███████╗██║██║   ██║██╔██╗ ██║ ");
        getLogger().info("██╔══╝   ██╔██╗ ██╔═══╝ ██╔══██║██║╚██╗██║╚════██║██║██║   ██║██║╚██╗██║ ");
        getLogger().info("███████╗██╔╝ ██╗██║     ██║  ██║██║ ╚████║███████║██║╚██████╔╝██║ ╚████║ ");
        getLogger().info("╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝╚═╝  ╚═══╝╚══════╝╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ");
        getLogger().info("                                                                         ");
        getLogger().info("                               Networks                                  ");
        getLogger().info("                            Author: Sefiraat                             ");
        getLogger().info("                           Networks Expansion                            ");
        getLogger().info("                       Chineseization: ybw0014                           ");
        getLogger().info("                     Author: yitoudaidai, tinalness                      ");
        getLogger().info("                   Internationalization: Magiauy, Sky                    ");
        getLogger().info("              If you encounter any bug, please feedback at:              ");
        getLogger().info("   Github (Global): https://github.com/magiauy/NetworksExpansion/issues  ");
        getLogger().info("  Github (Chinese): https://github.com/ytdd9527/NetworksExpansion/issues ");
        getLogger().info("                   Discord: https://discord.gg/M2yRf7VV3X                ");
        getLogger().info("     When using this plugin, please do not fork the project directly.    ");
        getLogger().info("                    Use /stop to avoid any data lost                     ");
        getLogger().info("#########################################################################");
    }


    public void setupIntegrations() {
        if (supportedPluginManager.isSlimeHud()) {
            getLogger().info("SlimeHUD was installed, registering related function！");
            try {
                HudCallbacks.setup();
            } catch (NoClassDefFoundError e) {
                getLogger().severe("You must update SlimeHUD to enable network functions.");
            }
        }
        if (supportedPluginManager.isNetheopoiesis()) {
            getLogger().info("Netheopoiesis was installed, registering related function!");
            try {
                NetheoPlants.setup();
            } catch (NoClassDefFoundError e) {
                getLogger().warning("You must update Netheoposiesis to enable network functions.");
            }
        }
    }

    public void setupMetrics() {
        final Metrics metrics = new Metrics(this, 13644);

        AdvancedPie networksChart = new AdvancedPie("networks", () -> {
            Map<String, Integer> networksMap = new HashMap<>();
            networksMap.put("Number of networks", NetworkController.getNetworks().size());
            return networksMap;
        });

        metrics.addCustomChart(networksChart);
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return MessageFormat.format("https://github.com/{0}/{1}/issues/", this.username, this.repo);
    }

    @Nonnull
    public String getWikiURL() {
        return MessageFormat.format("https://slimefun-addons-wiki.guizhanss.cn/networks/{0}/{1}", this.username, this.repo);
    }
}