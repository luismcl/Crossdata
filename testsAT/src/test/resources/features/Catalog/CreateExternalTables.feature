Feature: [CROSSDATA-251]Create External tables
  Background:
  Given Drop the spark tables

  Scenario: [CROSSDATA-268]Create a Cassandra table(Not exists keyspace and Table)
    Given I drop cassandra keyspace 'externalkeyspace'
    When I execute 'CREATE EXTERNAL TABLE externalkeyspace.externaltable (id INT, name STRING) USING com.stratio.crossdata.connector.cassandra OPTIONS (keyspace 'externalkeyspace', cluster '${CASSANDRA_CLUSTER}', pushdown "true", spark_cassandra_connection_host '${CASSANDRA_HOST}',primary_key_string 'id', with_replication "{'class' : 'SimpleStrategy', 'replication_factor' : 3}")'
    Then an exception 'IS NOT' thrown
    And The table 'externaltable' exists in cassandra keyspace 'externalkeyspace'

  Scenario: [CROSSDATA-268]Create a Cassandra table(exists keyspace) without keyspace properties
    Given I create a  cassandra keyspace 'externalkeyspace'
    When I execute 'CREATE EXTERNAL TABLE externalkeyspace.externaltable (id INT, name STRING) USING com.stratio.crossdata.connector.cassandra OPTIONS (keyspace 'externalkeyspace', cluster '${CASSANDRA_CLUSTER}', pushdown "true", spark_cassandra_connection_host '${CASSANDRA_HOST}',primary_key_string 'id')'
    Then an exception 'IS NOT' thrown
    And The table 'externaltable' exists in cassandra keyspace 'externalkeyspace'

  Scenario: [CROSSDATA-268]Create a Cassandra table(exists keyspace and exists table) without keyspace properties
    Given I create a  cassandra table 'externaltable' over keyspace 'externalkeyspace'
    When I execute 'CREATE EXTERNAL TABLE externalkeyspace.externaltable (id INT, name STRING) USING com.stratio.crossdata.connector.cassandra OPTIONS (keyspace 'externalkeyspace', cluster '${CASSANDRA_CLUSTER}', pushdown "true", spark_cassandra_connection_host '${CASSANDRA_HOST}',primary_key_string 'id')'
    Then an exception 'IS' thrown

  Scenario Outline: [CROSSDATA-268]Create a Cassandra table(exists keyspace) without keyspace properties
    Given I create a  cassandra keyspace 'externalkeyspace'
    When I execute 'CREATE EXTERNAL TABLE externalkeyspace.externaltable (id INT, name <type>) USING com.stratio.crossdata.connector.cassandra OPTIONS (keyspace 'externalkeyspace', cluster '${CASSANDRA_CLUSTER}', pushdown "true", spark_cassandra_connection_host '${CASSANDRA_HOST}',primary_key_string 'id')'
    Then an exception 'IS NOT' thrown
    And The table 'externaltable' exists in cassandra keyspace 'externalkeyspace'
     Examples:
        | type                  |
        | INT                   |
        | LONG                  |
        | FLOAT                 |
        | DOUBLE                |
        | DECIMAL               |
        | STRING                |
        | BINARY                |
        | BOOLEAN               |
        | TIMESTAMP             |
        | ARRAY<STRING>         |
        | MAP<INT, INT>         |
