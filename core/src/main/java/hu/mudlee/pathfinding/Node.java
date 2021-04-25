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

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;

public class Node implements IndexedNode<Node> {
  /** Index that needs to be unique for every node and starts from 0. */
  private final int mIndex;

  /** X pos of node. */
  public final int mX;
  /** Y pos of node. */
  public final int mY;

  /** The neighbours of this node. i.e to which node can we travel to from here. */
  Array<Connection<Node>> mConnections = new Array<Connection<Node>>();

  /** @param aIndex needs to be unique for every node and starts from 0. */
  public Node(int aX, int aY, int aIndex) {
    mIndex = aIndex;
    mX = aX;
    mY = aY;
  }

  @Override
  public int getIndex() {
    return mIndex;
  }

  @Override
  public Array<Connection<Node>> getConnections() {
    return mConnections;
  }

  public void addNeighbour(Node aNode) {
    if (null != aNode) {
      mConnections.add(new DefaultConnection<Node>(this, aNode));
    }
  }

  public String toString() {
    return String.format("Index:%d x:%d y:%d", mIndex, mX, mY);
  }
}
