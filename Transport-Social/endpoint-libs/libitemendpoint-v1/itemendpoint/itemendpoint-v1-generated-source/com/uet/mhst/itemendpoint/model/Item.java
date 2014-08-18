/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-07-22 21:53:01 UTC)
 * on 2014-08-18 at 06:23:24 UTC 
 * Modify at your own risk.
 */

package com.uet.mhst.itemendpoint.model;

/**
 * Model definition for Item.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the itemendpoint. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Item extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<Comment> comment;

  static {
    // hack to force ProGuard to consider Comment used, since otherwise it would be stripped out
    // see http://code.google.com/p/google-api-java-client/issues/detail?id=528
    com.google.api.client.util.Data.nullOf(Comment.class);
  }

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String content;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Key id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String idFB;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private GeoPt point;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer status;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime time;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<Vote> vote;

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<Comment> getComment() {
    return comment;
  }

  /**
   * @param comment comment or {@code null} for none
   */
  public Item setComment(java.util.List<Comment> comment) {
    this.comment = comment;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getContent() {
    return content;
  }

  /**
   * @param content content or {@code null} for none
   */
  public Item setContent(java.lang.String content) {
    this.content = content;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Key getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public Item setId(Key id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getIdFB() {
    return idFB;
  }

  /**
   * @param idFB idFB or {@code null} for none
   */
  public Item setIdFB(java.lang.String idFB) {
    this.idFB = idFB;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public GeoPt getPoint() {
    return point;
  }

  /**
   * @param point point or {@code null} for none
   */
  public Item setPoint(GeoPt point) {
    this.point = point;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getStatus() {
    return status;
  }

  /**
   * @param status status or {@code null} for none
   */
  public Item setStatus(java.lang.Integer status) {
    this.status = status;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getTime() {
    return time;
  }

  /**
   * @param time time or {@code null} for none
   */
  public Item setTime(com.google.api.client.util.DateTime time) {
    this.time = time;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<Vote> getVote() {
    return vote;
  }

  /**
   * @param vote vote or {@code null} for none
   */
  public Item setVote(java.util.List<Vote> vote) {
    this.vote = vote;
    return this;
  }

  @Override
  public Item set(String fieldName, Object value) {
    return (Item) super.set(fieldName, value);
  }

  @Override
  public Item clone() {
    return (Item) super.clone();
  }

}
