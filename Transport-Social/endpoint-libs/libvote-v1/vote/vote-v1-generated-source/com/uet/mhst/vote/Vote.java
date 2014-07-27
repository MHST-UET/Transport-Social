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
 * on 2014-07-27 at 11:19:22 UTC 
 * Modify at your own risk.
 */

package com.uet.mhst.vote;

/**
 * Service definition for Vote (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link VoteRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Vote extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.18.0-rc of the vote library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://transportsocial2014.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "vote/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Vote(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Vote(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "voteDw".
   *
   * This request holds the parameters needed by the vote server.  After setting any optional
   * parameters, call the {@link VoteDw#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public VoteDw voteDw(java.lang.Long id) throws java.io.IOException {
    VoteDw result = new VoteDw(id);
    initialize(result);
    return result;
  }

  public class VoteDw extends VoteRequest<Void> {

    private static final String REST_PATH = "voteDw/{id}";

    /**
     * Create a request for the method "voteDw".
     *
     * This request holds the parameters needed by the the vote server.  After setting any optional
     * parameters, call the {@link VoteDw#execute()} method to invoke the remote operation. <p> {@link
     * VoteDw#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
     * be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected VoteDw(java.lang.Long id) {
      super(Vote.this, "POST", REST_PATH, null, Void.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public VoteDw setAlt(java.lang.String alt) {
      return (VoteDw) super.setAlt(alt);
    }

    @Override
    public VoteDw setFields(java.lang.String fields) {
      return (VoteDw) super.setFields(fields);
    }

    @Override
    public VoteDw setKey(java.lang.String key) {
      return (VoteDw) super.setKey(key);
    }

    @Override
    public VoteDw setOauthToken(java.lang.String oauthToken) {
      return (VoteDw) super.setOauthToken(oauthToken);
    }

    @Override
    public VoteDw setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (VoteDw) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public VoteDw setQuotaUser(java.lang.String quotaUser) {
      return (VoteDw) super.setQuotaUser(quotaUser);
    }

    @Override
    public VoteDw setUserIp(java.lang.String userIp) {
      return (VoteDw) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public VoteDw setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public VoteDw set(String parameterName, Object value) {
      return (VoteDw) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "voteUp".
   *
   * This request holds the parameters needed by the vote server.  After setting any optional
   * parameters, call the {@link VoteUp#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public VoteUp voteUp(java.lang.Long id) throws java.io.IOException {
    VoteUp result = new VoteUp(id);
    initialize(result);
    return result;
  }

  public class VoteUp extends VoteRequest<Void> {

    private static final String REST_PATH = "voteUp/{id}";

    /**
     * Create a request for the method "voteUp".
     *
     * This request holds the parameters needed by the the vote server.  After setting any optional
     * parameters, call the {@link VoteUp#execute()} method to invoke the remote operation. <p> {@link
     * VoteUp#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
     * be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected VoteUp(java.lang.Long id) {
      super(Vote.this, "POST", REST_PATH, null, Void.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public VoteUp setAlt(java.lang.String alt) {
      return (VoteUp) super.setAlt(alt);
    }

    @Override
    public VoteUp setFields(java.lang.String fields) {
      return (VoteUp) super.setFields(fields);
    }

    @Override
    public VoteUp setKey(java.lang.String key) {
      return (VoteUp) super.setKey(key);
    }

    @Override
    public VoteUp setOauthToken(java.lang.String oauthToken) {
      return (VoteUp) super.setOauthToken(oauthToken);
    }

    @Override
    public VoteUp setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (VoteUp) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public VoteUp setQuotaUser(java.lang.String quotaUser) {
      return (VoteUp) super.setQuotaUser(quotaUser);
    }

    @Override
    public VoteUp setUserIp(java.lang.String userIp) {
      return (VoteUp) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public VoteUp setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public VoteUp set(String parameterName, Object value) {
      return (VoteUp) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link Vote}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Vote}. */
    @Override
    public Vote build() {
      return new Vote(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link VoteRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setVoteRequestInitializer(
        VoteRequestInitializer voteRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(voteRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}