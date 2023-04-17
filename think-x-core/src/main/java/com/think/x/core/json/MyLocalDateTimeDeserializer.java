package com.think.x.core.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.think.x.core.constants.DateConstants.*;


/**
 * Json格式 LocalDateTime 类型的参数，支持6中格式
 *
 * @date 2020/6/18 上午10:50
 */
public class MyLocalDateTimeDeserializer extends JSR310DateTimeDeserializerBase<LocalDateTime> {
    public static final MyLocalDateTimeDeserializer INSTANCE = new MyLocalDateTimeDeserializer();
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    /**
     * 以下是支持的6种参数格式
     */
    private static final DateTimeFormatter DEFAULT_DATE_FORMAT_DTF = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    private static final DateTimeFormatter DEFAULT_DATE_FORMAT_EN_DTF = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_EN);
    private static final DateTimeFormatter SLASH_DATE_FORMAT_DTF = DateTimeFormatter.ofPattern(SLASH_DATE_FORMAT);
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMAT_DTF = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMAT_EN_DTF = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT_EN);
    private static final DateTimeFormatter SLASH_DATE_TIME_FORMAT_DTF = DateTimeFormatter.ofPattern(SLASH_DATE_TIME_FORMAT);


    private MyLocalDateTimeDeserializer() {
        this(DEFAULT_FORMATTER);
    }

    public MyLocalDateTimeDeserializer(DateTimeFormatter formatter) {
        super(LocalDateTime.class, formatter);
    }

    protected MyLocalDateTimeDeserializer(MyLocalDateTimeDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    @Override
    protected MyLocalDateTimeDeserializer withLeniency(Boolean leniency) {
        return new MyLocalDateTimeDeserializer(this, leniency);
    }

    @Override
    protected JSR310DateTimeDeserializerBase<LocalDateTime> withShape(JsonFormat.Shape shape) {
        return null;
    }

    @Override
    protected JSR310DateTimeDeserializerBase<LocalDateTime> withDateFormat(DateTimeFormatter formatter) {
        return new LocalDateTimeDeserializer(formatter);
    }

    private LocalDateTime convert(String source) {
        if (source.matches(DEFAULT_DATE_FORMAT_MATCHES)) {
            return LocalDateTime.of(LocalDate.parse(source, DEFAULT_DATE_FORMAT_DTF), LocalTime.MIN);
        }
        if (source.matches(DEFAULT_DATE_FORMAT_EN_MATCHES)) {
            return LocalDateTime.of(LocalDate.parse(source, DEFAULT_DATE_FORMAT_EN_DTF), LocalTime.MIN);
        }
        if (source.matches(SLASH_DATE_FORMAT_MATCHES)) {
            return LocalDateTime.of(LocalDate.parse(source, SLASH_DATE_FORMAT_DTF), LocalTime.MIN);
        }
        if (source.matches(DEFAULT_DATE_TIME_FORMAT_MATCHES)) {
            return LocalDateTime.parse(source, DEFAULT_DATE_TIME_FORMAT_DTF);
        }
        if (source.matches(DEFAULT_DATE_TIME_FORMAT_EN_MATCHES)) {
            return LocalDateTime.parse(source, DEFAULT_DATE_TIME_FORMAT_EN_DTF);
        }
        if (source.matches(SLASH_DATE_TIME_FORMAT_MATCHES)) {
            return LocalDateTime.parse(source, SLASH_DATE_TIME_FORMAT_DTF);
        }
        return null;
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        // 字符串
        if (parser.hasTokenId(JsonTokenId.ID_STRING)) {
            String string = parser.getText().trim();
            if (string.length() == 0) {
                return null;
            }

            try {
                if (_formatter == null) {
                    return convert(string);
                }
                if (_formatter == DEFAULT_FORMATTER) {
                    // JavaScript by default includes time and zone in JSON serialized Dates (UTC/ISO instant format).
                    if (string.length() > 10 && string.charAt(10) == 'T') {
                        if (string.endsWith("Z")) {
                            return LocalDateTime.ofInstant(Instant.parse(string), ZoneOffset.UTC);
                        } else {
                            return LocalDateTime.parse(string, DEFAULT_FORMATTER);
                        }
                    }
                    return convert(string);
                }

                return LocalDateTime.parse(string, this._formatter);
            } catch (DateTimeException e) {
                return _handleDateTimeException(context, e, string);
            }
        }
        // 数组
        if (parser.isExpectedStartArrayToken()) {
            JsonToken t = parser.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return null;
            }
            if ((t == JsonToken.VALUE_STRING || t == JsonToken.VALUE_EMBEDDED_OBJECT) && context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                final LocalDateTime parsed = deserialize(parser, context);
                if (parser.nextToken() != JsonToken.END_ARRAY) {
                    handleMissingEndArrayForSingle(parser, context);
                }
                return parsed;
            }
            if (t == JsonToken.VALUE_NUMBER_INT) {
                LocalDateTime result;

                int year = parser.getIntValue();
                int month = parser.nextIntValue(-1);
                int day = parser.nextIntValue(-1);
                int hour = parser.nextIntValue(-1);
                int minute = parser.nextIntValue(-1);

                t = parser.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    result = LocalDateTime.of(year, month, day, hour, minute);
                } else {
                    int second = parser.getIntValue();
                    t = parser.nextToken();
                    if (t == JsonToken.END_ARRAY) {
                        result = LocalDateTime.of(year, month, day, hour, minute, second);
                    } else {
                        int partialSecond = parser.getIntValue();
                        if (partialSecond < 1_000 &&
                                !context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS))
                            partialSecond *= 1_000_000; // value is milliseconds, convert it to nanoseconds
                        if (parser.nextToken() != JsonToken.END_ARRAY) {
                            throw context.wrongTokenException(parser, handledType(), JsonToken.END_ARRAY,
                                    "Expected array to end");
                        }
                        result = LocalDateTime.of(year, month, day, hour, minute, second, partialSecond);
                    }
                }
                return result;
            }
            context.reportInputMismatch(handledType(),
                    "Unexpected token (%s) within Array, expected VALUE_NUMBER_INT",
                    t);
        }
        // 数字
        if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            return Instant.ofEpochMilli(parser.getLongValue()).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        }
        // 没看懂这个是啥
        if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
            return (LocalDateTime) parser.getEmbeddedObject();
        }

        return _handleUnexpectedToken(context, parser, "当前参数需要数组、字符串、时间戳。");
    }

}
