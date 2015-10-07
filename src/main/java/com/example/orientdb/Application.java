package com.example.orientdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Application {
  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  private final OrientGraphFactory orientGraphFactory;

  public Application() {
    this.orientGraphFactory = new OrientGraphFactory("remote:n1/testdb0", "admin", "admin");
    this.orientGraphFactory.setupPool(5, 10);
  }

  public void run() throws Exception {
    ObjectMapper om = new ObjectMapper();
    FriendList fl = om.readValue(new URL("http://pastebin.com/raw.php?i=g15uP20s"), FriendList.class);

    OrientGraph orientGraph = null;
    orientGraph = orientGraphFactory.getTx();

    Stopwatch sw0 = Stopwatch.createStarted();

    try {
      Stopwatch sw1 = Stopwatch.createStarted();
      orientGraph = orientGraphFactory.getTx();
      LOGGER.info("getTx: {}ms.", sw1.stop().elapsed(TimeUnit.MILLISECONDS));

      Stopwatch sw2 = Stopwatch.createStarted();
      Vertex flv = orientGraph.addVertex("class:FriendList", "uuid", fl.getUuid());
      for (Group group: fl.getGroups()) {
        Vertex gv = orientGraph.addVertex("class:Group", "uuid", group.getUuid());
        for (String username: group.getUsernames()) {
          Vertex uv = getOrCreateUser(orientGraph, username);
          uv.addEdge("IsIn", gv);
        }
        gv.addEdge("IsGroupOf", flv);
      }
      LOGGER.info("inserts: {}ms", sw2.stop().elapsed(TimeUnit.MILLISECONDS));

      Stopwatch sw3 = Stopwatch.createStarted();
      orientGraph.commit();
      LOGGER.info("commit: {}ms", sw3.stop().elapsed(TimeUnit.MILLISECONDS));
    } catch (Exception ex) {
      if (orientGraph != null) {
        orientGraph.rollback();
      }
      throw ex;
    } finally {
      LOGGER.info("{}ms.", sw0.stop().elapsed(TimeUnit.MILLISECONDS));
      if (orientGraph != null) {
        orientGraph.shutdown();
      }
    }
  }

  public static void main(String[] args) throws Exception {
    (new Application()).run();
  }

  private static Vertex getOrCreateUser(OrientGraph orientGraph, String username) {
    Vertex result = orientGraph.getVertexByKey("User.username", username);

    if (result == null) {
      result = orientGraph.addVertex("class:User", "username", username);
    }

    return result;
  }
}
