package com.griefcraft.server;/*
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

import com.griefcraft.CommonProxy;
import com.griefcraft.Engine;
import com.griefcraft.ForgeConsoleCommandSender;
import com.griefcraft.ForgeServerInfo;
import com.griefcraft.ForgeServerLayer;
import com.griefcraft.LWC;
import com.griefcraft.ServerLayer;
import com.griefcraft.SimpleEngine;
import com.griefcraft.listeners.ForgeListener;
import com.griefcraft.util.config.FileConfiguration;
import net.minecraftforge.common.MinecraftForge;

/**
 * Proxy used by just the server
 */
public class LWCServerProxy extends CommonProxy {

    @Override
    public void init() {
        System.out.println("LWC => init()");
        FileConfiguration.CONFIG_PATH = "mods/LWC/";
        MinecraftForge.EVENT_BUS.register(new ForgeListener(LWC.instance));

        // create an engine
        ServerLayer layer = new ForgeServerLayer();
        Engine engine = SimpleEngine.createEngine(layer, new ForgeServerInfo(), new ForgeConsoleCommandSender());
        LWC.instance.setupServer(engine, layer);
    }

}
