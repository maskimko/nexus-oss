package org.sonatype.nexus.coreui

import groovy.transform.ToString


/**
 * Repository reference exchange object.
 *
 * @since 3.0
 */
@ToString(includePackage = false, includeNames = true)
class RepositoryReferenceXO
    extends ReferenceXO
{
  String type
  String format
}
