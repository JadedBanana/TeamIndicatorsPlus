package com.github.jadedbanana.teamindicatorsplus.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;

public class KeyBinds {

    private final KeyBinding[] keyBinding;
    private final KeyEvents keyEvents;


    /*
    Constructor.
     */
    public KeyBinds() {
        // Initialize key bindings
        this.keyBinding = new KeyBinding[2];
        this.setKeybinds();

        // Initialize key events
        this.keyEvents = new KeyEvents(this.keyBinding);
        ClientTickEvents.END_CLIENT_TICK.register(client -> this.keyEvents.KeyBindsEvents(client));
    }


    /*
    Create keybind objects.
    Openconfig is set to page up by default.
    Toggle is not bound by default.
     */
    public void setKeybinds() {
        this.keyBinding[0] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.teamcoloredgui.openconfig",
                266,
                "key.teamcoloredgui.category"
        ));
        this.keyBinding[1] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.teamcoloredgui.toggle",
                -1,
                "key.teamcoloredgui.category"
        ));
    }

}