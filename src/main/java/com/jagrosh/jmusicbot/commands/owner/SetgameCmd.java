/*
 * Copyright 2017 John Grosh <john.a.grosh@gmail.com>.
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
package com.jagrosh.jmusicbot.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.commands.OwnerCommand;
import net.dv8tion.jda.api.entities.Activity;

/**
 *
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class SetgameCmd extends OwnerCommand
{
    public SetgameCmd(Bot bot)
    {
        this.name = "تعيين_اللعبة";
        this.help = "يحدد اللعبة التي يلعبها البوت";
        this.arguments = "[action] [game]";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = false;
        this.children = new OwnerCommand[]{
            new SetlistenCmd(),
            new SetstreamCmd(),
            new SetwatchCmd()
        };
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        String title = event.getArgs().toLowerCase().startsWith("playing") ? event.getArgs().substring(7).trim() : event.getArgs();
        try
        {
            event.getJDA().getPresence().setActivity(title.isEmpty() ? null : Activity.playing(title));
            event.reply(event.getClient().getSuccess()+" **"+event.getSelfUser().getName()
                    +"** هي "+(title.isEmpty() ? "لم يعد هناك أي شيء قيد التشغيل." : "يعمل الآن `"+title+"`"));
        }
        catch(Exception e)
        {
            event.reply(event.getClient().getError()+" تعذر تعيين اللعبة!");
        }
    }
    
    private class SetstreamCmd extends OwnerCommand
    {
        private SetstreamCmd()
        {
            this.name = "بث";
            this.aliases = new String[]{"twitch","streaming"};
            this.help = "يحدد بث مباشر يقوم البوت ببثه";
            this.arguments = "<اسم_المستخدم> <اللعبة>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            String[] parts = event.getArgs().split("\\s+", 2);
            if(parts.length<2)
            {
                event.replyError("يرجى إدخال اسم مستخدم تويتش واسم اللعبة للبث");
                return;
            }
            try
            {
                event.getJDA().getPresence().setActivity(Activity.streaming(parts[1], "https://twitch.tv/"+parts[0]));
                event.replySuccess("**"+event.getSelfUser().getName()
                        +"** يقوم الآن ببث `"+parts[1]+"`");
            }
            catch(Exception e)
            {
                event.reply(event.getClient().getError()+" تعذر تعيين اللعبة!");
            }
        }
    }
    
    private class SetlistenCmd extends OwnerCommand
    {
        private SetlistenCmd()
        {
            this.name = "استماع";
            this.aliases = new String[]{"listening"};
            this.help = "يحدد أن البوت يستمع لشيء معين";
            this.arguments = "<العنوان>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            if(event.getArgs().isEmpty())
            {
                event.replyError("يرجى إدخال عنوان للاستماع إليه!");
                return;
            }
            String title = event.getArgs().toLowerCase().startsWith("to") ? event.getArgs().substring(2).trim() : event.getArgs();
            try
            {
                event.getJDA().getPresence().setActivity(Activity.listening(title));
                event.replySuccess("**"+event.getSelfUser().getName()+"** is now listening to `"+title+"`");
            } catch(Exception e) {
                event.reply(event.getClient().getError()+" تعذر تعيين اللعبة!");
            }
        }
    }
    
    private class SetwatchCmd extends OwnerCommand
    {
        private SetwatchCmd()
        {
            this.name = "مشاهدة";
            this.aliases = new String[]{"watching"};
            this.help = "يحدد أن البوت يشاهد شيئًا معينًا";
            this.arguments = "<العنوان>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            if(event.getArgs().isEmpty())
            {
                event.replyError("يرجى إدخال عنوان لمشاهدته!");
                return;
            }
            String title = event.getArgs();
            try
            {
                event.getJDA().getPresence().setActivity(Activity.watching(title));
                event.replySuccess("**"+event.getSelfUser().getName()+"** is now watching `"+title+"`");
            } catch(Exception e) {
                event.reply(event.getClient().getError()+" تعذر تعيين اللعبة!");
            }
        }
    }
}
