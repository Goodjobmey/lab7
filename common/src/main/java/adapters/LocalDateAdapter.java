package adapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
  @Override
  public JsonElement serialize(LocalDate date, Type typeOfSrc,
                               JsonSerializationContext context) {
    return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
  }

  @Override
  public LocalDate deserialize(JsonElement json, Type type,
                               JsonDeserializationContext context) throws JsonParseException {
    return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
  }
}