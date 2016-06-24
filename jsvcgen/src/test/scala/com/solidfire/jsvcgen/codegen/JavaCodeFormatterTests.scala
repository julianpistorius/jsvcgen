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
package com.solidfire.jsvcgen.codegen

import com.solidfire.jsvcgen.codegen.TestHelper._
import org.scalatest.{Matchers, WordSpec}


class JavaCodeFormatterTests extends WordSpec with Matchers {

  val formatter = new JavaCodeFormatter( buildOptions.copy( namespace = "testNameSpace" ), buildServiceDefinition )

  "getTypeName(String, Boolean)" should {
    "map primitives types when primitives are allowed" in {
      formatter.getTypeName( "boolean", canBePrimitive = true ) should be( "boolean" )
      formatter.getTypeName( "integer", canBePrimitive = true ) should be( "long" )
      formatter.getTypeName( "long", canBePrimitive = true ) should be( "long" )
      formatter.getTypeName( "number", canBePrimitive = true ) should be( "double" )
      formatter.getTypeName( "float", canBePrimitive = true ) should be( "double" )
    }

    "map wrapper types when primitives are not allowed" in {
      formatter.getTypeName( "boolean", canBePrimitive = false ) should be( "Boolean" )
      formatter.getTypeName( "integer", canBePrimitive = false ) should be( "Long" )
      formatter.getTypeName( "long", canBePrimitive = false ) should be( "Long" )
      formatter.getTypeName( "number", canBePrimitive = false ) should be( "Double" )
      formatter.getTypeName( "float", canBePrimitive = false ) should be( "Double" )
    }

    "map string, regardless of case, to String" in {
      formatter.getTypeName( "String", canBePrimitive = false ) should be( "String" )
      formatter.getTypeName( "String", canBePrimitive = true ) should be( "String" )
    }

    "map object, regardless of case, to Map<String, Object>" in {
      formatter.getTypeName( "object", canBePrimitive = false ) should be( "java.util.Map<String, Object>" )
    }

    "map Object to Object" in {
    }

    "map types to alias primitive types" in {
      formatter.getTypeName( "yesOrNo", canBePrimitive = true ) should be( "boolean" )
      formatter.getTypeName( "uint64", canBePrimitive = true ) should be( "long" )
      formatter.getTypeName( "uint32", canBePrimitive = true ) should be( "long" )
      formatter.getTypeName( "size_t", canBePrimitive = true ) should be( "long" )
      formatter.getTypeName( "ID", canBePrimitive = true ) should be( "long" )
      formatter.getTypeName( "bigID", canBePrimitive = true ) should be( "long" )
      formatter.getTypeName( "smallID", canBePrimitive = true ) should be( "long" )
      formatter.getTypeName( "ratio", canBePrimitive = true ) should be( "double" )
      formatter.getTypeName( "precision", canBePrimitive = true ) should be( "double" )
    }

    "map types to base alias types" in {
      formatter.getTypeName( "yesOrNo" ) should be( "Boolean" )
      formatter.getTypeName( "uint64" ) should be( "Long" )
      formatter.getTypeName( "uint32" ) should be( "Long" )
      formatter.getTypeName( "size_t" ) should be( "Long" )
      formatter.getTypeName( "ID" ) should be( "Long" )
      formatter.getTypeName( "bigID" ) should be( "Long" )
      formatter.getTypeName( "smallID" ) should be( "Long" )
      formatter.getTypeName( "ratio" ) should be( "Double" )
      formatter.getTypeName( "precision" ) should be( "Double" )
      formatter.getTypeName( "name" ) should be( "String" )
      formatter.getTypeName( "UUID" ) should be( "String" )
    }

    "map optional types to alias types even if canBePrimitive" in {
      formatter.getTypeName( "maybeYesOrNo", canBePrimitive = true ) should be( "Boolean" )
      formatter.getTypeName( "someID", canBePrimitive = true ) should be( "Long" )
      formatter.getTypeName( "someBigID", canBePrimitive = true ) should be( "Long" )
      formatter.getTypeName( "someSmallID", canBePrimitive = true ) should be( "Long" )
      formatter.getTypeName( "someRatio", canBePrimitive = true ) should be( "Double" )
      formatter.getTypeName( "somePrecision", canBePrimitive = true ) should be( "Double" )
    }

    "map non-aliased, non-primitive types to capitalized case" in {
      formatter.getTypeName( "myType" ) should be( "MyType" )
    }
  }

  "getTypeName(TypeDefinition)" should {
    "map types to alias primitive types" in {
      formatter.getTypeName( yesOrNo ) should be( "boolean" )
      formatter.getTypeName( uint64 ) should be( "long" )
      formatter.getTypeName( uint32 ) should be( "long" )
      formatter.getTypeName( size_t ) should be( "long" )
      formatter.getTypeName( ID ) should be( "long" )
      formatter.getTypeName( bigID ) should be( "long" )
      formatter.getTypeName( smallID ) should be( "long" )
      formatter.getTypeName( ratio ) should be( "double" )
      formatter.getTypeName( precision ) should be( "double" )
    }


    "map optional types to alias wrapper types" in {
      formatter.getTypeName( maybeYesOrNo ) should be( "Boolean" )
      formatter.getTypeName( someID ) should be( "Long" )
      formatter.getTypeName( someBigID ) should be( "Long" )
      formatter.getTypeName( someSmallID ) should be( "Long" )
      formatter.getTypeName( someRatio ) should be( "Double" )
      formatter.getTypeName( somePrecision ) should be( "Double" )
    }
  }

  "getTypeName(TypeUse)" should {
    "map types to alias primitive types" in {
      formatter.getTypeName( yesOrNo.alias.get ) should be( "boolean" )
      formatter.getTypeName( uint64.alias.get ) should be( "long" )
      formatter.getTypeName( uint32.alias.get ) should be( "long" )
      formatter.getTypeName( size_t.alias.get ) should be( "long" )
      formatter.getTypeName( ID.alias.get ) should be( "long" )
      formatter.getTypeName( bigID.alias.get ) should be( "long" )
      formatter.getTypeName( smallID.alias.get ) should be( "long" )
      formatter.getTypeName( ratio.alias.get ) should be( "double" )
      formatter.getTypeName( precision.alias.get ) should be( "double" )
    }

    "map array types to alias primitive array types" in {
      formatter.getTypeName( yesOrNo.alias.get.copy( isArray = true ) ) should be( "Boolean[]" )
      formatter.getTypeName( uint64.alias.get.copy( isArray = true ) ) should be( "Long[]" )
      formatter.getTypeName( uint32.alias.get.copy( isArray = true ) ) should be( "Long[]" )
      formatter.getTypeName( size_t.alias.get.copy( isArray = true ) ) should be( "Long[]" )
      formatter.getTypeName( ID.alias.get.copy( isArray = true ) ) should be( "Long[]" )
      formatter.getTypeName( bigID.alias.get.copy( isArray = true ) ) should be( "Long[]" )
      formatter.getTypeName( smallID.alias.get.copy( isArray = true ) ) should be( "Long[]" )
      formatter.getTypeName( ratio.alias.get.copy( isArray = true ) ) should be( "Double[]" )
      formatter.getTypeName( precision.alias.get.copy( isArray = true ) ) should be( "Double[]" )
    }

    "map optional types to alias wrapper types" in {
      formatter.getTypeName( maybeYesOrNo.alias.get ) should be( "Optional<Boolean>" )
      formatter.getTypeName( someID.alias.get ) should be( "Optional<Long>" )
      formatter.getTypeName( someBigID.alias.get ) should be( "Optional<Long>" )
      formatter.getTypeName( someSmallID.alias.get ) should be( "Optional<Long>" )
      formatter.getTypeName( someRatio.alias.get ) should be( "Optional<Double>" )
      formatter.getTypeName( somePrecision.alias.get ) should be( "Optional<Double>" )
    }

    "map optional array types to alias wrapper array types" in {
      formatter.getTypeName( maybeYesOrNo.alias.get.copy( isArray = true ) ) should be( "Optional<Boolean[]>" )
      formatter.getTypeName( someID.alias.get.copy( isArray = true ) ) should be( "Optional<Long[]>" )
      formatter.getTypeName( someBigID.alias.get.copy( isArray = true ) ) should be( "Optional<Long[]>" )
      formatter.getTypeName( someSmallID.alias.get.copy( isArray = true ) ) should be( "Optional<Long[]>" )
      formatter.getTypeName( someRatio.alias.get.copy( isArray = true ) ) should be( "Optional<Double[]>" )
      formatter.getTypeName( somePrecision.alias.get.copy( isArray = true ) ) should be( "Optional<Double[]>" )
    }
  }
}