package prd.organisation.domain

import groovy.transform.Canonical

@Canonical
class User {
    String firstName
    String lastName
    String email
    String[] roles
}
