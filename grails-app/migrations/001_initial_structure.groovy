databaseChangeLog = {

    changeSet(author: "Tabby (generated)", id: "1544703168480-1") {
        createTable(tableName: "contact_information") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "contact_informationPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "address", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "BINARY(255)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "BINARY(255)") {
                constraints(nullable: "false")
            }

            column(name: "organisation_id", type: "BIGINT")

            column(name: "user_id", type: "BIGINT")

            column(name: "contact_id", type: "BINARY") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-2") {
        createTable(tableName: "domain") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "domainPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "host", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "organisation_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "domain_id", type: "BINARY") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-3") {
        createTable(tableName: "organisation") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "organisationPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "sra_id", type: "VARCHAR(255)")

            column(name: "organisation_id", type: "BINARY") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "BINARY(255)") {
                constraints(nullable: "false")
            }

            column(name: "company_number", type: "VARCHAR(255)")

            column(name: "sra_regulated", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "url", type: "VARCHAR(255)")

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-4") {
        createTable(tableName: "payment_account") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "payment_accountPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "payment_account_id", type: "BINARY") {
                constraints(nullable: "false")
            }

            column(name: "pba_number", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "organisation_id", type: "BIGINT")

            column(name: "user_id", type: "BIGINT")
        }
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-5") {
        createTable(tableName: "professional_user") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "professional_userPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "BINARY") {
                constraints(nullable: "false")
            }

            column(name: "first_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "organisation_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "last_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "email_id", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-6") {
        addUniqueConstraint(columnNames: "contact_id", constraintName: "UC_CONTACT_INFORMATIONCONTACT_ID_COL", tableName: "contact_information")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-7") {
        addUniqueConstraint(columnNames: "domain_id", constraintName: "UC_DOMAINDOMAIN_ID_COL", tableName: "domain")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-8") {
        addUniqueConstraint(columnNames: "name", constraintName: "UC_ORGANISATIONNAME_COL", tableName: "organisation")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-9") {
        addUniqueConstraint(columnNames: "organisation_id", constraintName: "UC_ORGANISATIONORGANISATION_ID_COL", tableName: "organisation")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-10") {
        addUniqueConstraint(columnNames: "payment_account_id", constraintName: "UC_PAYMENT_ACCOUNTPAYMENT_ACCOUNT_ID_COL", tableName: "payment_account")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-11") {
        addUniqueConstraint(columnNames: "pba_number", constraintName: "UC_PAYMENT_ACCOUNTPBA_NUMBER_COL", tableName: "payment_account")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-12") {
        addUniqueConstraint(columnNames: "email_id", constraintName: "UC_PROFESSIONAL_USEREMAIL_ID_COL", tableName: "professional_user")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-13") {
        addUniqueConstraint(columnNames: "user_id", constraintName: "UC_PROFESSIONAL_USERUSER_ID_COL", tableName: "professional_user")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-14") {
        addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "contact_information", constraintName: "FK9bl2tisoqqdsnq2aot0nog62q", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "organisation")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-15") {
        addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "professional_user", constraintName: "FK9c85ohvq1lhm2bvs19vhfsnvs", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "organisation")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-16") {
        addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "payment_account", constraintName: "FKdpltvob5nu5lj92rjiavt85w1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "organisation")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-17") {
        addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "domain", constraintName: "FKej7ita5swmd3dxnlvoo24d1p", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "organisation")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-18") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "contact_information", constraintName: "FKiauc1sacjfvacgx7gaoukrjld", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "professional_user")
    }

    changeSet(author: "Tabby (generated)", id: "1544703168480-19") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "payment_account", constraintName: "FKq7731bgvat2x2b1g45ncvg97p", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "professional_user")
    }
}
