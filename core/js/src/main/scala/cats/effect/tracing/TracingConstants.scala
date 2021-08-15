/*
 * Copyright 2020-2021 Typelevel
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

package cats.effect.tracing

import scala.scalajs.js

private object TracingConstants {

  @inline def stackTracingMode: String =
    // This style of check is needed only for global variables
    (if (js.typeOf(js.Dynamic.global.process) != "undefined")
       Some(js.Dynamic.global.process)
     else None)
      .flatMap(p => Option(p.env).filterNot(js.isUndefined))
      .flatMap { env =>
        Option(env.CATS_EFFECT_TRACING_MODE)
          .filterNot(js.isUndefined)
          .orElse(Option(env.REACT_APP_CATS_EFFECT_TRACING_MODE).filterNot(js.isUndefined))
      }
      .map(_.asInstanceOf[String])
      .filterNot(_.isEmpty)
      .getOrElse("cached")

  @inline def isCachedStackTracing: Boolean = stackTracingMode.equalsIgnoreCase("cached")

  @inline def isFullStackTracing: Boolean = stackTracingMode.equalsIgnoreCase("full")

  @inline def isStackTracing: Boolean = isFullStackTracing || isCachedStackTracing

}
