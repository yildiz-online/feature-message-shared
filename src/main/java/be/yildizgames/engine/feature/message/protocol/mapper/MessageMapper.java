/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2019 Grégory Van den Borre
 *
 *  More infos available: https://engine.yildiz-games.be
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  SOFTWARE.
 *
 */

package be.yildizgames.engine.feature.message.protocol.mapper;

import be.yildizgames.common.mapping.BooleanMapper;
import be.yildizgames.common.mapping.LongMapper;
import be.yildizgames.common.mapping.ObjectMapper;
import be.yildizgames.common.mapping.Separator;
import be.yildizgames.common.mapping.model.PlayerIdMapper;
import be.yildizgames.engine.feature.message.Message;

/**
 * @author Grégory Van den Borre
 */
public class MessageMapper implements ObjectMapper<Message> {

    private static final String REP_AMP = "A5A7";

    private static final String REP_HSH = "H5H6";

    private static final String REP_UND = "U5K9";

    private static final String REP_AT = "T5G7";

    private static final MessageMapper INSTANCE = new MessageMapper();

    private MessageMapper() {
        super();
    }

    public static MessageMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public final Message from(String s) {
        String[] v = s.split(Separator.VAR_SEPARATOR);
        try {
            return new Message(PlayerIdMapper.getInstance().from(v[0]),
                    PlayerIdMapper.getInstance().from(v[1]),
                    v[2].replace(REP_AMP, "&")
                        .replace(REP_AT, "@")
                        .replace(REP_HSH, "#")
                        .replace(REP_UND, "_"),
                    LongMapper.getInstance().from(v[3]),
                    BooleanMapper.getInstance().from(v[4]));
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public final String to(Message message) {
        return PlayerIdMapper.getInstance().to(message.getSender())
                + Separator.VAR_SEPARATOR
                + PlayerIdMapper.getInstance().to(message.getReceiver())
                + Separator.VAR_SEPARATOR
                + message.getMessage()
                        .replace("&", REP_AMP)
                        .replace("#", REP_HSH)
                        .replace("_", REP_UND)
                        .replace("@", REP_AT)
                + Separator.VAR_SEPARATOR
                + LongMapper.getInstance().to(message.getDate())
                + Separator.VAR_SEPARATOR
                + BooleanMapper.getInstance().to(message.isRead());
    }
}
