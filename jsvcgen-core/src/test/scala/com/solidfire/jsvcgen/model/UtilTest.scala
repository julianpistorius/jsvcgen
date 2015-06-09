/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 **/
package com.solidfire.jsvcgen.model

import com.solidfire.jsvcgen.model.Util._
import org.scalatest._
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import scala.util.Random.nextInt

class UtilSpec extends FlatSpec with Matchers {
  "validateNamespace" should "validate a simple namespace with no periods" in {
    validateNamespace( "nodots" )
  }

  "validateNamespace" should "validate a nested namespace" in {
    validateNamespace( "yes.there_is.s0m37h1ng.h3r3" )
  }

  "validateNamespace" should "validate an all underscore namespace -- REALLY?!?!?!" in {
    validateNamespace( "__._._._______._" )
  }

  "validateNamespace" should "throw ValidationException on empty" in {
    a[ValidationException] should be thrownBy {
      validateNamespace( "" )
    }
  }

  "validateNamespace" should "throw ValidationException on blank" in {
    a[ValidationException] should be thrownBy {
      validateNamespace( "     " )
    }
  }

  "validateNamespace" should "throw ValidationException on surounding whitespace" in {
    a[ValidationException] should be thrownBy {
      validateNamespace( "  valid   " )
    }
  }

  "validateNamespace" should "throw ValidationException on internal whitespace" in {
    a[ValidationException] should be thrownBy {
      validateNamespace( "val id" )
    }
  }

  "validateNamespace" should "throw ValidationException on starts with dot" in {
    a[ValidationException] should be thrownBy {
      validateNamespace( ".startswithdot" )
    }
  }

  "validateNamespace" should "throw ValidationException on ends with dot" in {
    a[ValidationException] should be thrownBy {
      validateNamespace( "endswithdot." )
    }
  }

  "validateNamespace" should "throw ValidationException on double dots" in {
    a[ValidationException] should be thrownBy {
      validateNamespace( "with..dot" )
    }
  }

  "validateNamespace" should "throw ValidationException on numeric" in {
    a[ValidationException] should be thrownBy {
      validateNamespace( "987" )
    }
  }

  "validateNamespace" should "throw ValidationException on numeric prefix" in {
    a[ValidationException] should be thrownBy {
      validateNamespace( "987ebin" )
    }
  }

  "validateNamespace" should "throw ValidationException on internal numeric prefix" in {
    a[ValidationException] should be thrownBy {
      validateNamespace( "com.987ebin" )
    }
  }
}

import org.scalacheck.Gen

object AsciiNamespaceGenerator {
  /** Generates an alphanumerical character with underscore */
  def alphaUnderscoreChar = Gen.frequency( (1, Gen.const( '_' )), (9, Gen.alphaChar) )

  /** Generates an alphanumerical character with underscore */
  def alphaNumUnderscoreChar = Gen.frequency( (1, Gen.numChar), (9, alphaUnderscoreChar) )

  val asciiNameGen: Gen[String] = for {
    s0 ← alphaUnderscoreChar
    s1 ← Gen.listOfN( nextInt( 20 ), alphaUnderscoreChar )
  } yield (s0 :: s1.toList).mkString

  val asciiNamespaceGen: Gen[String] = for {
    s0 ← Gen.listOfN( nextInt( 10 ), asciiNameGen )
  } yield s0.mkString( "." )
}

class UtilPropertiesTest extends PropSpec with GeneratorDrivenPropertyChecks with ShouldMatchers {

  implicit override val generatorDrivenConfig = PropertyCheckConfig( minSize = 100, maxSize = 200 )

  property( "Test ascii namespace isValidNamespace" ) {
    import AsciiNamespaceGenerator._
    forAll( asciiNamespaceGen ) { n: String ⇒
      isValidNamespace( n ) should be( true )
    }
  }
}

