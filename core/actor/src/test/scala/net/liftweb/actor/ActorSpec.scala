/*
 * Copyright 2011 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.liftweb
package actor

import org.specs2.mutable._

import common._


/**
 * Systems under specification for Lift Actor.
 */
object ActorSpec extends Specification {
  "Actor Specification".title
  
  "A Scala Actor" should {
    "support common features" in commonFeatures(new MyScalaActor)
  }

  "A Java Actor" should {
    "support common features" in commonFeatures(new MyJavaActor)
  }

  private def commonFeatures(actor: LiftActor) = {

    "allow setting and getting of a value" in {
      val a = actor
      a ! Set(33)
      a !? Get()
      (a.!?(50, Get())) must be_===(Full(Answer(33))).eventually
    }

    "allow setting and getting of a value with subclass of Get()" in {
      val a = actor
      a ! Set(33)
      a ! new FunnyGet()
      (a.!?(50L, new FunnyGet())) must be_===(Full(Answer(33))).eventually
    }

    "allow adding of a value" in {
      val a = actor
      a ! Set(33)
      (a !< Add(44)).get(50) must be_===(Full(Answer(77))).eventually
    }

    "allow subtracting of a value" in {
      val a = actor
      a ! Set(33)
      (a !< Sub(11)).get(50) must be_===(Full(Answer(22))).eventually
    }

    "properly timeout" in {
      val a = actor
      (a !< Set(33)).get(50) must be_===(Empty).eventually
    }
  }

}


case class Add(num: Int)
case class Sub(num: Int)
case class Set(num: Int)

case class Get()
class FunnyGet() extends Get()

case class Answer(num: Int)
