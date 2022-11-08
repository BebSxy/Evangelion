package dev.evangelion.api.manager.miscellaneous;

import dev.evangelion.api.manager.friend.Friend;
import com.google.gson.JsonArray;
import dev.evangelion.client.values.impl.ValueBind;
import java.awt.Color;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.Value;
import java.util.ArrayList;
import dev.evangelion.api.manager.element.Element;
import com.google.gson.Gson;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.GsonBuilder;
import java.io.File;
import com.google.gson.JsonObject;
import java.io.InputStream;
import java.util.Iterator;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import java.nio.file.OpenOption;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.module.Module;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.io.IOException;

public class ConfigManager
{
    public void load() {
        try {
            this.loadModules();
            this.loadElements();
            this.loadPrefix();
            this.loadFriends();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    public void save() {
        try {
            if (!Files.exists(Paths.get("Evangelion/", new String[0]), new LinkOption[0])) {
                Files.createDirectories(Paths.get("Evangelion/", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            }
            if (!Files.exists(Paths.get("Evangelion/Modules/", new String[0]), new LinkOption[0])) {
                Files.createDirectories(Paths.get("Evangelion/Modules/", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            }
            if (!Files.exists(Paths.get("Evangelion/Elements/", new String[0]), new LinkOption[0])) {
                Files.createDirectories(Paths.get("Evangelion/Elements/", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            }
            if (!Files.exists(Paths.get("Evangelion/Client/", new String[0]), new LinkOption[0])) {
                Files.createDirectories(Paths.get("Evangelion/Client/", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            }
            for (final Module.Category category : Module.Category.values()) {
                if (category != Module.Category.HUD) {
                    if (!Files.exists(Paths.get("Evangelion/Modules/" + category.getName() + "/", new String[0]), new LinkOption[0])) {
                        Files.createDirectories(Paths.get("Evangelion/Modules/" + category.getName() + "/", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
                    }
                }
            }
            this.saveModules();
            this.saveElements();
            this.savePrefix();
            this.saveFriends();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    public void attach() {
        Runtime.getRuntime().addShutdownHook(new SaveThread());
    }
    
    public void loadModules() throws IOException {
        for (final Module module : Evangelion.MODULE_MANAGER.getModules()) {
            if (!Files.exists(Paths.get("Evangelion/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json", new String[0]), new LinkOption[0])) {
                continue;
            }
            final InputStream stream = Files.newInputStream(Paths.get("Evangelion/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json", new String[0]), new OpenOption[0]);
            JsonObject moduleJson;
            try {
                moduleJson = new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject();
            }
            catch (IllegalStateException exception) {
                exception.printStackTrace();
                Evangelion.LOGGER.error(module.getName());
                continue;
            }
            if (moduleJson.get("Name") == null) {
                continue;
            }
            if (moduleJson.get("Status") == null) {
                continue;
            }
            if (moduleJson.get("Status").getAsBoolean()) {
                module.enable(false);
            }
            final JsonObject valueJson = moduleJson.get("Values").getAsJsonObject();
            this.loadValues(valueJson, module.getValues());
            stream.close();
        }
    }
    
    public void saveModules() throws IOException {
        for (final Module module : Evangelion.MODULE_MANAGER.getModules()) {
            if (Files.exists(Paths.get("Evangelion/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json", new String[0]), new LinkOption[0])) {
                final File file = new File("Evangelion/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json");
                file.delete();
            }
            Files.createFile(Paths.get("Evangelion/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final JsonObject moduleJson = new JsonObject();
            final JsonObject valueJson = new JsonObject();
            moduleJson.add("Name", (JsonElement)new JsonPrimitive(module.getName()));
            moduleJson.add("Status", (JsonElement)new JsonPrimitive(Boolean.valueOf(module.isToggled())));
            this.saveValues(valueJson, module.getValues());
            moduleJson.add("Values", (JsonElement)valueJson);
            final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("Evangelion/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json"), StandardCharsets.UTF_8);
            writer.write(gson.toJson(new JsonParser().parse(moduleJson.toString())));
            writer.close();
        }
    }
    
    public void loadElements() throws IOException {
        for (final Element element : Evangelion.ELEMENT_MANAGER.getElements()) {
            if (!Files.exists(Paths.get("Evangelion/Elements/" + element.getName() + ".json", new String[0]), new LinkOption[0])) {
                continue;
            }
            final InputStream stream = Files.newInputStream(Paths.get("Evangelion/Elements/" + element.getName() + ".json", new String[0]), new OpenOption[0]);
            JsonObject elementJson;
            try {
                elementJson = new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject();
            }
            catch (IllegalStateException exception) {
                exception.printStackTrace();
                Evangelion.LOGGER.error(element.getName());
                continue;
            }
            if (elementJson.get("Name") == null || elementJson.get("Status") == null) {
                continue;
            }
            if (elementJson.get("Positions") == null) {
                continue;
            }
            if (elementJson.get("Status").getAsBoolean()) {
                element.enable(false);
            }
            final JsonObject valueJson = elementJson.get("Values").getAsJsonObject();
            final JsonObject positionJson = elementJson.get("Positions").getAsJsonObject();
            this.loadValues(valueJson, element.getValues());
            if (positionJson.get("X") != null && positionJson.get("Y") != null) {
                element.frame.setX(positionJson.get("X").getAsFloat());
                element.frame.setY(positionJson.get("Y").getAsFloat());
            }
            stream.close();
        }
    }
    
    public void saveElements() throws IOException {
        for (final Element element : Evangelion.ELEMENT_MANAGER.getElements()) {
            if (Files.exists(Paths.get("Evangelion/Elements/" + element.getName() + ".json", new String[0]), new LinkOption[0])) {
                final File file = new File("Evangelion/Elements/" + element.getName() + ".json");
                file.delete();
            }
            Files.createFile(Paths.get("Evangelion/Elements/" + element.getName() + ".json", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final JsonObject elementJson = new JsonObject();
            final JsonObject valueJson = new JsonObject();
            final JsonObject positionJson = new JsonObject();
            elementJson.add("Name", (JsonElement)new JsonPrimitive(element.getName()));
            elementJson.add("Status", (JsonElement)new JsonPrimitive(Boolean.valueOf(element.isToggled())));
            this.saveValues(valueJson, element.getValues());
            positionJson.add("X", (JsonElement)new JsonPrimitive((Number)element.frame.getX()));
            positionJson.add("Y", (JsonElement)new JsonPrimitive((Number)element.frame.getY()));
            elementJson.add("Values", (JsonElement)valueJson);
            elementJson.add("Positions", (JsonElement)positionJson);
            final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("Evangelion/Elements/" + element.getName() + ".json"), StandardCharsets.UTF_8);
            writer.write(gson.toJson(new JsonParser().parse(elementJson.toString())));
            writer.close();
        }
    }
    
    private void loadValues(final JsonObject valueJson, final ArrayList<Value> values) {
        for (final Value value : values) {
            final JsonElement dataObject = valueJson.get(value.getName());
            if (dataObject != null && dataObject.isJsonPrimitive()) {
                if (value instanceof ValueBoolean) {
                    ((ValueBoolean)value).setValue(dataObject.getAsBoolean());
                }
                else if (value instanceof ValueNumber) {
                    if (((ValueNumber)value).getType() == 1) {
                        ((ValueNumber)value).setValue(dataObject.getAsInt());
                    }
                    else if (((ValueNumber)value).getType() == 2) {
                        ((ValueNumber)value).setValue(dataObject.getAsDouble());
                    }
                    else {
                        if (((ValueNumber)value).getType() != 3) {
                            continue;
                        }
                        ((ValueNumber)value).setValue(dataObject.getAsFloat());
                    }
                }
                else if (value instanceof ValueEnum) {
                    ((ValueEnum)value).setValue(((ValueEnum)value).getEnum(dataObject.getAsString()));
                }
                else if (value instanceof ValueString) {
                    ((ValueString)value).setValue(dataObject.getAsString());
                }
                else if (value instanceof ValueColor) {
                    ((ValueColor)value).setValue(new Color(dataObject.getAsInt()));
                    if (valueJson.get(value.getName() + "-Rainbow") != null) {
                        ((ValueColor)value).setRainbow(valueJson.get(value.getName() + "-Rainbow").getAsBoolean());
                    }
                    if (valueJson.get(value.getName() + "-Alpha") != null) {
                        ((ValueColor)value).setValue(new Color(((ValueColor)value).getValue().getRed(), ((ValueColor)value).getValue().getGreen(), ((ValueColor)value).getValue().getBlue(), valueJson.get(value.getName() + "-Alpha").getAsInt()));
                    }
                    if (valueJson.get(value.getName() + "-Sync") == null) {
                        continue;
                    }
                    ((ValueColor)value).setSync(valueJson.get(value.getName() + "-Sync").getAsBoolean());
                }
                else {
                    if (!(value instanceof ValueBind)) {
                        continue;
                    }
                    ((ValueBind)value).setValue(dataObject.getAsInt());
                }
            }
        }
    }
    
    private void saveValues(final JsonObject valueJson, final ArrayList<Value> values) {
        for (final Value value : values) {
            if (value instanceof ValueBoolean) {
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive(Boolean.valueOf(((ValueBoolean)value).getValue())));
            }
            else if (value instanceof ValueNumber) {
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive(((ValueNumber)value).getValue()));
            }
            else if (value instanceof ValueEnum) {
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive(((ValueEnum)value).getValue().name()));
            }
            else if (value instanceof ValueString) {
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive(((ValueString)value).getValue()));
            }
            else if (value instanceof ValueColor) {
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive((Number)((ValueColor)value).getValue().getRGB()));
                valueJson.add(value.getName() + "-Alpha", (JsonElement)new JsonPrimitive((Number)((ValueColor)value).getValue().getAlpha()));
                valueJson.add(value.getName() + "-Rainbow", (JsonElement)new JsonPrimitive(Boolean.valueOf(((ValueColor)value).isRainbow())));
                valueJson.add(value.getName() + "-Sync", (JsonElement)new JsonPrimitive(Boolean.valueOf(((ValueColor)value).isSync())));
            }
            else {
                if (!(value instanceof ValueBind)) {
                    continue;
                }
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive((Number)((ValueBind)value).getValue()));
            }
        }
    }
    
    public void loadPrefix() throws IOException {
        if (!Files.exists(Paths.get("Evangelion/Client/Prefix.json", new String[0]), new LinkOption[0])) {
            return;
        }
        final InputStream stream = Files.newInputStream(Paths.get("Evangelion/Client/Prefix.json", new String[0]), new OpenOption[0]);
        final JsonObject prefixJson = new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject();
        if (prefixJson.get("Prefix") == null) {
            return;
        }
        Evangelion.COMMAND_MANAGER.setPrefix(prefixJson.get("Prefix").getAsString());
        stream.close();
    }
    
    public void savePrefix() throws IOException {
        if (Files.exists(Paths.get("Evangelion/Client/Prefix.json", new String[0]), new LinkOption[0])) {
            final File file = new File("Evangelion/Client/Prefix.json");
            file.delete();
        }
        Files.createFile(Paths.get("Evangelion/Client/Prefix.json", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final JsonObject prefixJson = new JsonObject();
        prefixJson.add("Prefix", (JsonElement)new JsonPrimitive(Evangelion.COMMAND_MANAGER.getPrefix()));
        final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("Evangelion/Client/Prefix.json"), StandardCharsets.UTF_8);
        writer.write(gson.toJson(new JsonParser().parse(prefixJson.toString())));
        writer.close();
    }
    
    public void loadFriends() throws IOException {
        if (!Files.exists(Paths.get("Evangelion/Client/Friends.json", new String[0]), new LinkOption[0])) {
            return;
        }
        final InputStream stream = Files.newInputStream(Paths.get("Evangelion/Client/Friends.json", new String[0]), new OpenOption[0]);
        final JsonObject mainObject = new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject();
        if (mainObject.get("Friends") == null) {
            return;
        }
        final JsonArray friendArray = mainObject.get("Friends").getAsJsonArray();
        friendArray.forEach(friend -> Evangelion.FRIEND_MANAGER.addFriend(friend.getAsString()));
        stream.close();
    }
    
    public void saveFriends() throws IOException {
        if (Files.exists(Paths.get("Evangelion/Client/Friends.json", new String[0]), new LinkOption[0])) {
            final File file = new File("Evangelion/Client/Friends.json");
            file.delete();
        }
        Files.createFile(Paths.get("Evangelion/Client/Friends.json", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final JsonObject mainObject = new JsonObject();
        final JsonArray friendArray = new JsonArray();
        for (final Friend friend : Evangelion.FRIEND_MANAGER.getFriends()) {
            friendArray.add(friend.getName());
        }
        mainObject.add("Friends", (JsonElement)friendArray);
        final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("Evangelion/Client/Friends.json"), StandardCharsets.UTF_8);
        writer.write(gson.toJson(new JsonParser().parse(mainObject.toString())));
        writer.close();
    }
    
    public static class SaveThread extends Thread
    {
        @Override
        public void run() {
            Evangelion.CONFIG_MANAGER.save();
        }
    }
}
