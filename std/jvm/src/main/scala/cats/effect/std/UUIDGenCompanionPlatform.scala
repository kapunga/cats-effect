/*
 * Copyright 2020-2024 Typelevel
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

package cats.effect.std

import cats.{Applicative, Functor, Monoid}
import cats.data.{
  EitherT,
  IndexedReaderWriterStateT,
  IndexedStateT,
  IorT,
  Kleisli,
  OptionT,
  WriterT
}
import cats.effect.kernel.Sync

import java.util.UUID

private[std] trait UUIDGenCompanionPlatform extends UUIDGenCompanionPlatformLowPriority

private[std] trait UUIDGenCompanionPlatformLowPriority {

  @deprecated(
    "Put an implicit `SecureRandom.javaSecuritySecureRandom` into scope to get a more efficient `UUIDGen`, or directly call `UUIDGen.fromSecureRandom`",
    "3.6.0"
  )
  implicit def fromSync[F[_]](implicit ev: Sync[F]): UUIDGen[F] = new UUIDGen[F] {
    override final val randomUUID: F[UUID] =
      ev.blocking(UUID.randomUUID())
  }

  /**
   * [[UUIDGen]] instance built for `cats.data.EitherT` values initialized with any `F` data
   * type that also implements `UUIDGen`.
   */
  implicit def catsEitherTUUIDGen[F[_]: UUIDGen: Functor, L]: UUIDGen[EitherT[F, L, *]] =
    UUIDGen[F].mapK(EitherT.liftK)

  /**
   * [[UUIDGen]] instance built for `cats.data.Kleisli` values initialized with any `F` data
   * type that also implements `UUIDGen`.
   */
  implicit def catsKleisliUUIDGen[F[_]: UUIDGen, R]: UUIDGen[Kleisli[F, R, *]] =
    UUIDGen[F].mapK(Kleisli.liftK)

  /**
   * [[UUIDGen]] instance built for `cats.data.OptionT` values initialized with any `F` data
   * type that also implements `UUIDGen`.
   */
  implicit def catsOptionTUUIDGen[F[_]: UUIDGen: Functor]: UUIDGen[OptionT[F, *]] =
    UUIDGen[F].mapK(OptionT.liftK)

  /**
   * [[UUIDGen]] instance built for `cats.data.IndexedStateT` values initialized with any `F`
   * data type that also implements `UUIDGen`.
   */
  implicit def catsIndexedStateTUUIDGen[F[_]: UUIDGen: Applicative, S]
      : UUIDGen[IndexedStateT[F, S, S, *]] =
    UUIDGen[F].mapK(IndexedStateT.liftK)

  /**
   * [[UUIDGen]] instance built for `cats.data.WriterT` values initialized with any `F` data
   * type that also implements `UUIDGen`.
   */
  implicit def catsWriterTUUIDGen[
      F[_]: UUIDGen: Applicative,
      L: Monoid
  ]: UUIDGen[WriterT[F, L, *]] =
    UUIDGen[F].mapK(WriterT.liftK)

  /**
   * [[UUIDGen]] instance built for `cats.data.IorT` values initialized with any `F` data type
   * that also implements `UUIDGen`.
   */
  implicit def catsIorTUUIDGen[F[_]: UUIDGen: Functor, L]: UUIDGen[IorT[F, L, *]] =
    UUIDGen[F].mapK(IorT.liftK)

  /**
   * [[UUIDGen]] instance built for `cats.data.IndexedReaderWriterStateT` values initialized
   * with any `F` data type that also implements `UUIDGen`.
   */
  implicit def catsIndexedReaderWriterStateTUUIDGen[
      F[_]: UUIDGen: Applicative,
      E,
      L: Monoid,
      S
  ]: UUIDGen[IndexedReaderWriterStateT[F, E, L, S, S, *]] =
    UUIDGen[F].mapK(IndexedReaderWriterStateT.liftK)

}
