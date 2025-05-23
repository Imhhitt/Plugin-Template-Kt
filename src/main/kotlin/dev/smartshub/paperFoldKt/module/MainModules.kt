package dev.smartshub.paperFoldKt.module

import dev.smartshub.paperFoldKt.common.config.CacheConfig
import dev.smartshub.paperFoldKt.common.config.LanguageConfig
import dev.smartshub.paperFoldKt.common.config.LicenseConfig
import dev.smartshub.paperFoldKt.common.config.StorageConfig
import dev.smartshub.paperFoldKt.common.module.repo.LocalesRepository
import dev.smartshub.paperFoldKt.common.module.repo.StorageRepository
import dev.smartshub.paperFoldKt.common.service.ConfLoader
import dev.smartshub.paperFoldKt.config.MainConfig
import dev.smartshub.paperFoldKt.data.Players
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Table
import org.koin.dsl.module
import java.io.File

val localesRepositoryModule = module {
    single<LocalesRepository> {
        val plugin = get<JavaPlugin>()
        LocalesRepository(File(plugin.dataFolder, "locales/"))
    }
}

val storageRepositoryModule = module {
    single<StorageRepository> {
        val plugin = get<JavaPlugin>()
        StorageRepository(File(plugin.dataFolder, "storage/"))
    }
}

val pluginRepositoryModule = module {
    single<PluginRepository> {
        val plugin = get<JavaPlugin>()
        val repository = PluginRepository(plugin)
        repository.setup()
        repository
    }
}

val mainConfigModule = module { //TODO: Make it reloadable
    single<MainConfig> {
        val plugin = get<JavaPlugin>() //this will be provided by the platform-specific implementation
        val configFile = File(plugin.dataFolder, "config.yml")
//        val defaultConfig = plugin.getResource("config.yml")

        ConfLoader.load<MainConfig>(configFile)!!
    }
}

val licenseConfigModule = module {
    single<LicenseConfig> {
        val configuration = get<MainConfig>()
        configuration.license
    }
}

val cacheConfigModule = module {
    single<CacheConfig> {
        val configuration = get<MainConfig>()
        configuration.cache
    }
}

val languageConfigModule = module {
    single<LanguageConfig> {
        val configuration = get<MainConfig>()
        configuration.language
    }
}

val storageConfigModule = module {
    single<StorageConfig> {
        val configuration = get<MainConfig>()
        configuration.storage
    }
}

val entitiesModule = module {
    single<List<Table>> {
        listOf<Table>(Players)
    }
}

val loaderModule = module {
    single<Loader> {
        val plugin = get<JavaPlugin>()
        Loader(plugin)
    }
}

val debugModule = module {
    single<dev.smartshub.paperFoldKt.module.Debug> {
        val plugin = get<JavaPlugin>()
        dev.smartshub.paperFoldKt.module.Debug(plugin)
    }
}

//val snapshotsModule = module {
//    single<Snapshots> {
//        val cacheManager = get<CacheManager>()
//        val snapshots = cacheManager.createCache<Player, PlayerSnapshot>("playerSnapshots")
//        Snapshots(snapshots)
//    }
//}


val appModules = listOf(
    localesRepositoryModule,
    storageRepositoryModule,
    pluginRepositoryModule,
    mainConfigModule,
    licenseConfigModule,
    cacheConfigModule,
    languageConfigModule,
    storageConfigModule,
    entitiesModule,
    loaderModule,
    debugModule
)