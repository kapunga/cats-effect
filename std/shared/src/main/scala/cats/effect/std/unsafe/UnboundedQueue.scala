/*
 * Copyright 2020-2025 Typelevel
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

package cats
package effect
package std
package unsafe

import cats.syntax.all._

/**
 * A [[Queue]] which supports a side-effecting variant of `offer`, allowing impure code to add
 * elements to the queue without having to indirect through something like [[Dispatcher]].
 *
 * @see
 *   [[Queue.unsafeUnbounded]]
 */
trait UnboundedQueue[F[_], A]
    extends Queue[F, A]
    with BoundedQueue[F, A]
    with UnboundedQueueSink[F, A]

object UnboundedQueue {

  def apply[F[_]: kernel.Async, A]: F[UnboundedQueue[F, A]] =
    Queue.unsafeUnbounded[F, A]

  implicit def catsInvariantForUnboundedQueue[F[_]: Functor]: Invariant[Queue[F, *]] =
    new Invariant[Queue[F, *]] {
      override def imap[A, B](fa: Queue[F, A])(f: A => B)(g: B => A): Queue[F, B] =
        new Queue[F, B] {
          override def offer(b: B): F[Unit] =
            fa.offer(g(b))
          override def tryOffer(b: B): F[Boolean] =
            fa.tryOffer(g(b))
          override def take: F[B] =
            fa.take.map(f)
          override def tryTake: F[Option[B]] =
            fa.tryTake.map(_.map(f))
          override def size: F[Int] =
            fa.size
        }
    }
}
