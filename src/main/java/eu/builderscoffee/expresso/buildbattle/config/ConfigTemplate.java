package eu.builderscoffee.expresso.buildbattle.config;

public abstract class ConfigTemplate implements ConfigRequestable, ConfigResponsible {

    protected String type;

    public ConfigTemplate(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
