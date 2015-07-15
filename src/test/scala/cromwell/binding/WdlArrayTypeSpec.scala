package cromwell.binding

import cromwell.binding.types.{WdlStringType, WdlArrayType, WdlIntegerType}
import cromwell.binding.values.{WdlString, WdlArray, WdlInteger}
import org.scalatest.{FlatSpec, Matchers}
import spray.json.{JsNumber, JsArray}
import scala.util.{Success, Failure}

class WdlArrayTypeSpec extends FlatSpec with Matchers  {
  val intArray = WdlArray(WdlArrayType(WdlIntegerType), Seq(WdlInteger(1), WdlInteger(2), WdlInteger(3)))
  "WdlArray" should "stringify its value" in {
    intArray.toWdlString shouldEqual "[1, 2, 3]"
  }
  "WdlArrayType" should "coerce Seq(1,2,3) into a WdlArray" in {
    WdlArrayType(WdlIntegerType).coerceRawValue(Seq(1,2,3)) match {
      case Success(array) => array shouldEqual intArray
      case Failure(f) => fail(s"exception while coercing array: $f")
    }
  }
  it should "coerce a JsArray into a WdlArray" in {
    WdlArrayType(WdlIntegerType).coerceRawValue(JsArray(JsNumber(1), JsNumber(2), JsNumber(3))) match {
      case Success(array) => array shouldEqual intArray
      case Failure(f) => fail(s"exception while coercing JsArray: $f")
    }
  }
  it should "stringify its type" in {
    intArray.wdlType.toWdlString shouldEqual "Array[Int]"
  }
  it should "convert WDL source code to WdlArray" in {
    WdlArrayType(WdlIntegerType).fromRawString("[1,2,3]") shouldEqual intArray
  }
  it should "NOT successfully convert WDL source code to WdlArray if passed a bogus AST" in {
    try {
      WdlArrayType(WdlIntegerType).fromRawString("workflow wf{}")
      fail("should not have succeeded")
    } catch {
      case _: WdlExpressionException => // expected
    }
  }
  it should "NOT successfully convert WDL source code to WdlArray if passed a bogus AST (2)" in {
    try {
      WdlArrayType(WdlIntegerType).fromRawString("100")
      fail("should not have succeeded")
    } catch {
      case _: WdlExpressionException => // expected
    }
  }
  it should "NOT successfully convert WDL source code to WdlArray if passed a bogus AST (3)" in {
    try {
      WdlArrayType(WdlIntegerType).fromRawString("[1,stdout()]")
      fail("should not have succeeded")
    } catch {
      case _: WdlExpressionException => // expected
    }
  }
  it should "NOT successfully convert WDL source code to WdlArray if passed a bogus AST (4)" in {
    try {
      WdlArrayType(WdlIntegerType).fromRawString("[1,var]")
      fail("should not have succeeded")
    } catch {
      case _: WdlExpressionException => // expected
    }
  }
  it should "detect invalid array construction if there are mixed types" in {
    try {
      WdlArray(WdlArrayType(WdlStringType), Seq(WdlString("foo"), WdlInteger(2)))
      fail("Invalid array initialization should have failed")
    } catch {
      case _: UnsupportedOperationException => // expected
    }
  }
  it should "detect invalid array construction if type does not match the input array type" in {
    try {
      WdlArray(WdlArrayType(WdlStringType), Seq(WdlInteger(2)))
      fail("Invalid array initialization should have failed")
    } catch {
      case _: UnsupportedOperationException => // expected
    }
  }
}
