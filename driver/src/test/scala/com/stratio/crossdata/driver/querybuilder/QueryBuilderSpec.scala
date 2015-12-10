/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.driver.querybuilder

import java.sql.Date

import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class QueryBuilderSpec extends BaseXDTest {

  "The Query Builder" should "be able to build a completed query using strings" in {

    val query = select("col, '1', max(col)") from "table inner join table2 on a = b" where "a = b" groupBy "col" having "a = b" orderBy "col ASC" limit 5

    val expected = """
                     | SELECT col, '1', max(col)
                     | FROM table inner join table2 on a = b
                     | WHERE a = b
                     | GROUP BY col
                     | HAVING a = b
                     | ORDER BY col ASC
                     | LIMIT 5
                   """

    compareAfterFormatting(query.build, expected)
  }


  it should "be able to add a where clause on a limited query" in {

    val query = selectAll from 'table limit 1 where 'a < 5

    val expected = """
                     | SELECT * FROM table
                     | WHERE a < 5
                     | LIMIT 1
                   """

    compareAfterFormatting(query.build, expected)
  }

  it should "be able to add a where clause on a limited query which contains filters" in {

    val query = selectAll from 'table where 'a > 10 limit 1 where 'a < 5

    val expected = """
                     | SELECT * FROM table
                     | WHERE (a > 10) AND (a < 5)
                     | LIMIT 1
                   """

    compareAfterFormatting(query.build, expected)
  }


  it should "be able to join several queries" in {

    val query = (selectAll from 'table) unionAll (selectAll from 'table2) unionAll (selectAll from 'table3)

    val expected = """
                     | SELECT * FROM table
                     | UNION ALL
                     | SELECT * FROM table2
                     | UNION ALL
                     | SELECT * FROM table3
                   """

    compareAfterFormatting(query.build, expected)
  }

  it should "not allow to add a filter on a combined query" in {

    the[Error] thrownBy {
      (selectAll from 'table) unionAll (selectAll from 'table2) where "a = b"
    } should have message "Predicates cannot by applied to combined queries"

  }

  it should "be able to build a query containing predicates with objects" in {

    val query = selectAll from 'table where ('a < new Date(0) or 'a > 5)

    val expected = """
                     | SELECT * FROM table
                     | WHERE
                     | (a < '1970-01-01')
                     | OR
                     | (a > 5)
                   """

    compareAfterFormatting(query.build, expected)
  }

  it should "be able to build a query containing a subquery as a predicate" in {

    val query = select('c + 4).from('table).where('col === (select('c) from 't))

    val expected = """
                     | SELECT c + 4 FROM table
                     | WHERE col = ( SELECT c FROM t )
                   """
    compareAfterFormatting(query.build, expected)

  }

  it should "be able to build a completed query without distinct" in {

    val query = select(distinct('col)) from 'test where ('quantity > 10) groupBy 'age having ('age > 25) orderBy 'age limit 10

    val expected = """
                     | SELECT DISTINCT col
                     | FROM test
                     | WHERE quantity > 10
                     | GROUP BY age
                     | HAVING age > 25
                     | ORDER BY age
                     | LIMIT 10
                   """

    compareAfterFormatting(query.build, expected)

  }

  it should "be able to build a query with a subquery" in {

    val query = select("alias.name") from ((selectAll from 'table) as 'alias)

    val expected = """
                     | SELECT alias.name
                     | FROM (
                     | SELECT * FROM table
                     | ) AS alias
                   """

    compareAfterFormatting(query.build, expected)

  }

  it should "be able to build a query with an inner join clause" in {

    val query = select('name, 'age, 'quantity) from ('test innerJoin 'animals on "test.id = animals.id")

    val expected = """
                     | SELECT name, age, quantity
                     | FROM test
                     | JOIN animals
                     | ON test.id = animals.id
                   """

    compareAfterFormatting(query.build, expected)
  }

  it should "be able to build a query with a left semi join clause" in {

    val query = selectAll from ('t1 leftSemiJoin 't2)

    val expected = """
                     | SELECT * FROM t1
                     | LEFT SEMI JOIN
                     | t2
                   """
    println(query.build)
    println(expected)

    compareAfterFormatting(query.build, expected)
  }
  /*
  def leftOuterJoin(other: Relation): Join = Join(this, other, JoinType.LeftOuter)

  def rightOuterJoin(other: Relation): Join = Join(this, other, JoinType.RightOuter)

  def fullOuterJoin(other: Relation): Join = Join(this, other, JoinType.FullOuter)

  def leftSemiJoin(other: Relation): Join = Join(this, other, JoinType.LeftSemi)*/



  it should "be able to maintain user associations" in {

    val query = select (('a + 13) * ('hola + 2) + 5) from 'test

    val expected = """
                     | SELECT ((a +  13) * (hola + 2)) + 5
                     | FROM test
                   """

    compareAfterFormatting(query.build, expected)
  }




  def compareAfterFormatting(query: String, expected: String) = {
    formatOutput(query) should be(formatOutput(expected))
  }


  def formatOutput(query: String): String =
    query.stripMargin.replaceAll(System.lineSeparator(), " ").trim.replaceAll(" +", " ")







/* ALIAS columns, tables and queries
  def as(alias: String): Identifier = AliasIdentifier(this, alias)

  def as(alias: Symbol): Identifier = AliasIdentifier(this, alias.name)*/


/*  def unary_- : Expression = Minus(this)

  def unary_! : Predicate = Not(this)

  def +(other: Expression): Expression = Add(this, other)
  def add(other: Expression): Expression = Add(this, other)

  def -(other: Expression): Expression = Subtract(this, other)
  def substract(other: Expression): Expression = Subtract(this, other)

  def *(other: Expression): Expression = Multiply(this, other)

  def /(other: Expression): Expression = Divide(this, other)

  def %(other: Expression): Expression = Remainder(this, other)

  def &&(other: Expression): Predicate = And(this, other)
  def and(other: Expression): Predicate = And(this, other)

  def ||(other: Expression): Predicate = Or(this, other)
  def or(other: Expression): Predicate = Or(this, other)

  def <(other: Expression): Predicate = LessThan(this, other)

  def <=(other: Expression): Predicate = LessThanOrEqual(this, other)

  def >(other: Expression): Predicate = GreaterThan(this, other)

  def >=(other: Expression): Predicate = GreaterThanOrEqual(this, other)

  def ===(other: Expression): Predicate = Equal(this, other)

  def asc: SortOrder = SortOrder(this, Ascending)

  def desc: SortOrder = SortOrder(this, Descending)


  def in(list: Expression*): Expression = In(this, list: _*)

  def like(other: Expression): Expression = Like(this, other)


  def isNull: Predicate = IsNull(this)

  def isNotNull: Predicate = IsNotNull(this)*/


/*  def distinct(e: Expression*): Expression = Distinct(e: _*)

  def sum(e: Expression): Expression = Sum(e)

  def sumDistinct(e: Expression): Expression = SumDistinct(e)

  def count(e: Expression): Expression = Count(e)

  def countDistinct(e: Expression*): Expression = CountDistinct(e: _*)

  def approxCountDistinct(e: Expression, rsd: Double): Expression = ApproxCountDistinct(e, rsd)

  def avg(e: Expression): Expression = Avg(e)

  def min(e: Expression): Expression = Min(e)

  def max(e: Expression): Expression = Max(e)

  def abs(e: Expression): Expression = Abs(e)*/


/*  implicit def boolean2Literal(b: Boolean): Literal = Literal(b)
  implicit def byte2Literal(b: Byte): Literal = Literal(b)
  implicit def short2Literal(s: Short): Literal = Literal(s)
  implicit def int2Literal(i: Int): Literal = Literal(i)
  implicit def long2Literal(l: Long): Literal = Literal(l)
  implicit def float2Literal(f: Float): Literal = Literal(f)
  implicit def double2Literal(d: Double): Literal = Literal(d)
  implicit def string2Literal(s: String): Literal = Literal(s)
  implicit def date2Literal(d: Date): Literal = Literal(d)
  implicit def bigDecimal2Literal(d: BigDecimal): Literal = Literal(d.underlying())
  implicit def bigDecimal2Literal(d: java.math.BigDecimal): Literal = Literal(d)
  implicit def timestamp2Literal(t: Timestamp): Literal = Literal(t)
  implicit def binary2Literal(a: Array[Byte]): Literal = Literal(a)*/

/*  def unionAll(newQuery: RunnableQuery): CombinedQuery =
    generateCombinedQuery {
      computeCombinationInfo(newQuery, UnionAll, query => query.unionAll(newQuery))
    }

  def unionDistinct(newQuery: RunnableQuery): CombinedQuery =
    generateCombinedQuery {
      computeCombinationInfo(newQuery, UnionDistinct, query => query.unionDistinct(newQuery))
    }


  def intersect(newQuery: RunnableQuery): CombinedQuery =
    generateCombinedQuery {
      computeCombinationInfo(newQuery, Intersect, query => query.intersect(newQuery))
    }

  def except(newQuery: RunnableQuery): CombinedQuery =
    generateCombinedQuery {
      computeCombinationInfo(newQuery, Except, query => query.except(newQuery))
    }*/

}
