/*******************************************************************************
 * Copyright 2014 Christoffer Hindelid.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
  ******************************************************************************/
package hu.mudlee.pathfinding;

import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;

public class Graph extends DefaultIndexedGraph<Node> {
  /**
   * @param aSize Just an estimate of size. Will grow later if needed.
   */
  public Graph(int aSize) {
    super(aSize);
  }

  public void addNode(Node aNodes) {
    nodes.add(aNodes);
  }

  public Node getNode(int aIndex) {
    return nodes.get(aIndex);
  }
}
