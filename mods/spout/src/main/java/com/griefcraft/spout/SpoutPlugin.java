/*
 * Copyright (c) 2011, 2012, Tyler Blair
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package com.griefcraft.spout;

import com.griefcraft.Engine;
import com.griefcraft.ServerLayer;
import com.griefcraft.SimpleEngine;
import com.griefcraft.World;
import com.griefcraft.command.CommandContext;
import com.griefcraft.command.CommandException;
import com.griefcraft.command.CommandSender;
import com.griefcraft.entity.Player;
import com.griefcraft.spout.command.SpoutConsoleCommandSender;
import com.griefcraft.spout.listeners.PlayerListener;
import org.spout.api.Spout;
import org.spout.api.event.Listener;
import org.spout.api.plugin.CommonPlugin;

import java.util.logging.Logger;

public class SpoutPlugin extends CommonPlugin implements Listener {
    private Logger logger = null;

    /**
     * The LWC engine
     */
    private Engine engine;

    /**
     * The server layer
     */
    private final ServerLayer layer = new SpoutServerLayer(this);

    /**
     * Get the LWC engine
     *
     * @return
     */
    public Engine getInternalEngine() {
        return engine;
    }

    /**
     * Wrap a player object to a native version we can work with
     *
     * @param player
     * @return
     */
    public Player wrapPlayer(org.spout.api.entity.Player player) {
        return layer.getPlayer(player.getName());
    }

    /**
     * Get a world
     *
     * @param worldName
     * @return
     */
    public World getWorld(String worldName) {
        return layer.getWorld(worldName);
    }

    @Override
    public void onEnable() {
        logger = this.getLogger();

        // Create a new LWC engine
        engine = SimpleEngine.createEngine(layer, new SpoutServerInfo(), new SpoutConsoleCommandSender());

        // Register events
        Spout.getEngine().getEventManager().registerEvents(this, this);
        Spout.getEngine().getEventManager().registerEvents(new PlayerListener(this), this);

    }

    @Override
    public void onDisable() {
        engine.disable();
        engine = null;
    }

    /**
     * Command processor
     *
     * @param sender
     * @param message the name of the command followed by any arguments.
     * @return true if the command event should be cancelled
     */
    protected boolean _onCommand(CommandContext.Type type, CommandSender sender, String message) {
        // Normalize the command, removing any prepended /, etc
        message = normalizeCommand(message);

        // Separate the command and arguments
        int indexOfSpace = message.indexOf(' ');

        try {
            if (indexOfSpace != -1) {
                String command = message.substring(0, indexOfSpace);
                String arguments = message.substring(indexOfSpace + 1);

                return engine.getCommandHandler().handleCommand(new CommandContext(type, sender, command, arguments));
            } else { // No arguments
                return engine.getCommandHandler().handleCommand(new CommandContext(type, sender, message));
            }
        } catch (CommandException e) {
            // Notify the console
            logger.info("An error was encountered while processing a command: " + e.getMessage());
            e.printStackTrace();

            // Notify the player / console
            // TODO red this bitch up
            sender.sendMessage("[LWC] An internal error occurred while processing this command");

            // We failed.. oh we failed
            return false;
        }
    }

    /**
     * Normalize a command, making player and console commands appear to be the same format
     *
     * @param message
     * @return
     */
    private String normalizeCommand(String message) {
        // Remove a prepended /
        if (message.startsWith("/")) {
            if (message.length() == 1) {
                return "";
            } else {
                message = message.substring(1);
            }
        }

        return message.trim();
    }

}
