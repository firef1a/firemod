package dev.fire.features.plot;

import dev.fire.features.Feature;

import static dev.fire.features.Features.init;

public class ModeTracker extends Feature {
    public ModeTracker() {
        init("modetracker", "Internal Mode Tracker", "Tracks the mode the user is in.");
    }
}
