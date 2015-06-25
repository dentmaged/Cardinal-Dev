package in.twizmwaz.cardinal.module.modules.utils;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class UtilBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<UtilModule> load(Match match) {
        ModuleCollection<UtilModule> results = new ModuleCollection<UtilModule>();
        results.add(new UtilModule());
        return results;
    }

}
