package dev.evangelion.api.manager.miscellaneous;

import dev.evangelion.client.modules.miscellaneous.spammer.SpammerInitializer;
import dev.evangelion.api.utilities.sounds.SoundInitializer;

public class InitializerManager
{
    SoundInitializer SOUND_INITIALIZER;
    SpammerInitializer SPAMMER_INITIALIZER;
    
    public void initialize() {
        this.SOUND_INITIALIZER = new SoundInitializer();
        this.SPAMMER_INITIALIZER = new SpammerInitializer();
        this.SOUND_INITIALIZER.initialize();
        this.SPAMMER_INITIALIZER.initialize();
    }
}
