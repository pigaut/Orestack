package io.github.pigaut.orestack.advertise;

import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.node.section.*;

import java.net.*;

public class AdvertisementManager extends Manager {

    private String premiumReminder = "";

    public AdvertisementManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    public String getPremiumReminder() {
        return premiumReminder;
    }

    @Override
    public void loadData() {
        RootSection status = new RootSection(null, plugin.getConfigurator(), null);

        try {
            URL url = new URL("https://raw.githubusercontent.com/pigaut/Orestack/free-version/status.yml");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            status.loadFromStream(conn.getInputStream());
        } catch (Exception ignored) {
        }

        premiumReminder = status.getString("premium-reminder", StringColor.FORMATTER).orElse("");
    }

}
