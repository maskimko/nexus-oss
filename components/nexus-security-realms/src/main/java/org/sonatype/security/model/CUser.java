/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2015 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.security.model;

/**
 * A User is a security object, where, based upon its role, will
 * have certain privileged access to the system.
 *
 * Note: This class has been generated by modello and copied over
 */
@SuppressWarnings("all")
public class CUser
    implements java.io.Serializable, java.lang.Cloneable
{

  //--------------------------/
  //- Class/Member Variables -/
  //--------------------------/

  /**
   * The userId that will be used to gain access to the system.
   */
  private String id;

  /**
   * Users first name.
   */
  private String firstName;

  /**
   * Users last name.
   */
  private String lastName;

  /**
   * Users password, note this is just the hash of the password,
   * not the actual password.
   */
  private String password;

  /**
   * The current status of this user, active/locked/disabled.
   */
  private String status;

  /**
   * The user's email address.
   */
  private String email;

  private String version;

  //-----------/
  //- Methods -/
  //-----------/

  /**
   * Method clone.
   *
   * @return CUser
   */
  public CUser clone() {
    try {
      CUser copy = (CUser) super.clone();

      return copy;
    }
    catch (java.lang.Exception ex) {
      throw (java.lang.RuntimeException) new java.lang.UnsupportedOperationException(getClass().getName()
          + " does not support clone()").initCause(ex);
    }
  } //-- CUser clone()

  /**
   * Get the user's email address.
   *
   * @return String
   */
  public String getEmail() {
    return this.email;
  } //-- String getEmail()

  /**
   * Get users first name.
   *
   * @return String
   */
  public String getFirstName() {
    return this.firstName;
  } //-- String getFirstName()

  /**
   * Get the userId that will be used to gain access to the
   * system.
   *
   * @return String
   */
  public String getId() {
    return this.id;
  } //-- String getId()

  /**
   * Get users last name.
   *
   * @return String
   */
  public String getLastName() {
    return this.lastName;
  } //-- String getLastName()

  /**
   * Get users password, note this is just the hash of the
   * password, not the actual password.
   *
   * @return String
   */
  public String getPassword() {
    return this.password;
  } //-- String getPassword()

  /**
   * Get the current status of this user, active/locked/disabled.
   *
   * @return String
   */
  public String getStatus() {
    return this.status;
  } //-- String getStatus()

  /**
   * Set the user's email address.
   */
  public void setEmail(String email) {
    this.email = email;
  } //-- void setEmail( String )

  /**
   * Set users first name.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  } //-- void setFirstName( String )

  /**
   * Set the userId that will be used to gain access to the
   * system.
   */
  public void setId(String id) {
    this.id = id;
  } //-- void setId( String )

  /**
   * Set users last name.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  } //-- void setLastName( String )

  /**
   * Set users password, note this is just the hash of the
   * password, not the actual password.
   */
  public void setPassword(String password) {
    this.password = password;
  } //-- void setPassword( String )

  /**
   * Set the current status of this user, active/locked/disabled.
   */
  public void setStatus(String status) {
    this.status = status;
  } //-- void setStatus( String )

  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    this.version = version;
  }

  public static final String STATUS_DISABLED = "disabled";

  public static final String STATUS_ACTIVE = "active";

}
