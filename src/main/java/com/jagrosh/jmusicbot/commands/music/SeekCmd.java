/*
 * Copyright 2020 John Grosh <john.a.grosh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.audio.RequestMetadata;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import com.jagrosh.jmusicbot.utils.TimeUtil;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Whew., Inc.
 */
public class SeekCmd extends MusicCommand
{
    private final static Logger LOG = LoggerFactory.getLogger("التقديم");
    
    public SeekCmd(Bot bot)
    {
        super(bot);
        this.name = "تقديم";
        this.help = "يقدم أو يؤخر في الأغنية الحالية";
        this.arguments = "[+ | -] <HH:MM:SS | MM:SS | SS>|<0h0m0s | 0m0s | 0s>";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.beListening = true;
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
        AudioTrack playingTrack = handler.getPlayer().getPlayingTrack();
        if (!playingTrack.isSeekable())
        {
            event.replyError("لا يمكن التقديم في هذا المقطع.");
            return;
        }


        if (!DJCommand.checkDJPermission(event) && playingTrack.getUserData(RequestMetadata.class).getOwner() != event.getAuthor().getIdLong())
        {
            event.replyError("لا يمكنك التقديم في **" + playingTrack.getInfo().title + "** لأنك لم تقم بإضافتها!");
            return;
        }

        String args = event.getArgs();
        TimeUtil.SeekTime seekTime = TimeUtil.parseTime(args);
        if (seekTime == null)
        {
            event.replyError("تقديم غير صالح! الصيغة المتوقعة: " + arguments + "\nأمثلة: `1:02:23` `+1:10` `-90`, `1h10m`, `+90s`");
            return;
        }

        long currentPosition = playingTrack.getPosition();
        long trackDuration = playingTrack.getDuration();

        long seekMilliseconds = seekTime.relative ? currentPosition + seekTime.milliseconds : seekTime.milliseconds;
        if (seekMilliseconds > trackDuration)
        {
            event.replyError("لا يمكن التقديم إلى `" + TimeUtil.formatTime(seekMilliseconds) + "` لأن المقطع الحالي هو `" + TimeUtil.formatTime(trackDuration) + "` طويلة!");
            return;
        }
        
        try
        {
            playingTrack.setPosition(seekMilliseconds);
        }
        catch (Exception e)
        {
            event.replyError("حدث خطأ أثناء محاولة التقديم: " + e.getMessage());
            LOG.warn("فشل التقديم في المقطع " + playingTrack.getIdentifier(), e);
            return;
        }
        event.replySuccess("تم التقديم إلى `" + TimeUtil.formatTime(playingTrack.getPosition()) + "/" + TimeUtil.formatTime(playingTrack.getDuration()) + "`!");
    }
}
