package tech.v2c.minecraft.plugins.lode.EventNotify.global;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import tech.v2c.minecraft.plugins.lode.Lode;
import tech.v2c.minecraft.plugins.lode.tools.results.JsonResult;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class LodeWebSocketServer extends WebSocketServer {
    public static HashMap<Object, WebSocket> connPool = new HashMap<Object, WebSocket>();
    public static final boolean executeByWs = Lode.instance.getConfig().getConfigurationSection("EventListener").getBoolean("ExecuteByWs");

    public LodeWebSocketServer() {
        super(new InetSocketAddress(Lode.instance.getConfig().getConfigurationSection("Server").getString("IP"), Lode.instance.getConfig().getConfigurationSection("Server").getInt("WsPort")));
    }

    public static void SendMsg(JsonResult result) {
        LodeWebSocketServer.connPool.values().forEach(conn -> {
            conn.send(result);
        });
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        if (Lode.instance.isDebugMode) {
            Gson gson = new Gson();
            conn.send(gson.toJson(new JsonResult(null, 204, "Debug mode.")));
            connPool.put(conn.getDraft(), conn);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connPool.remove(conn.getDraft());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Gson gson = new Gson();
        WsMessage msg = gson.fromJson(message, WsMessage.class);
        if (msg.getAction().equalsIgnoreCase("auth")) {
            ConfigurationSection authConf = Lode.instance.getConfig().getConfigurationSection("Server").getConfigurationSection("Authentication");
            String userName = authConf.getString("UserName");
            String password = authConf.getString("Password");

            HashMap<String, Object> authInfo = msg.getParams();
            if (userName.equals(authInfo.get("userName")) && password.equals(authInfo.get("password"))) {
                if (!connPool.containsKey(conn.getDraft())) {
                    connPool.put(conn.getDraft(), conn);
                    conn.send(gson.toJson(new JsonResult(null, 204, "auth success!")));
                } else {
                    conn.send(gson.toJson(new JsonResult(null, 204, "already login.")));
                }
            } else {
                conn.send(gson.toJson(new JsonResult(null, 401, "auth failed!")));
            }

            return;
        }

        if (msg.getAction().equalsIgnoreCase("executeCmd")) {
            if (Lode.instance.isDebugMode || connPool.containsKey(conn.getDraft())) {
                if (executeByWs) {
                    Lode.instance.getServer().getScheduler().scheduleSyncDelayedTask(Lode.instance, () -> {
                        Lode.instance.getServer().dispatchCommand(Bukkit.getConsoleSender(), msg.getParams().get("command").toString());
                        conn.send(gson.toJson(new JsonResult(null, 204, "execute success.")));
                    });
                }
            } else {
                conn.send(gson.toJson(new JsonResult(null, 401, "need login!")));
            }

            return;
        }

        conn.send(gson.toJson(new JsonResult(null, 403, "unknown command.")));
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Gson gson = new Gson();
        conn.send(gson.toJson(new JsonResult("unknown command.", 403, ex.getMessage())));
    }

    public class WsMessage {
        private String action;
        private HashMap<String, Object> params;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public HashMap<String, Object> getParams() {
            return params;
        }

        public void setMessage(HashMap<String, Object> params) {
            this.params = params;
        }
    }
}
