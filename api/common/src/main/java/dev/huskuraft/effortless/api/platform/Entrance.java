package dev.huskuraft.effortless.api.platform;

public interface Entrance {

    String getId();

    Platform getPlatform();

    static Entrance getInstance() {
        return Instance.get();
    }

    default ContentFactory getContentFactory() {
        return ContentFactory.getInstance();
    }

    class Instance {
        private Instance() {
        }
        private static Entrance instance;
        public static Entrance get() {
            return Instance.instance;
        }
        public static void set(Entrance instance) {
            Instance.instance = instance;
        }
    }

}

