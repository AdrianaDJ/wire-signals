/*
 * Wire
 * Copyright (C) 2016 Wire Swiss GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.wire.signals

import org.threeten.bp.{Clock, Instant}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._
import org.threeten.bp.Instant.now

case class ClockSignal(interval: FiniteDuration, clock: Clock = Clock.systemUTC())
  extends SourceSignal[Instant](Some(now(clock))) {

  private var delay = CancellableFuture.successful({})

  def refresh(): Unit = if (wired) {
    publish(now(clock))
    delay.cancel()
    delay = CancellableFuture.delayed(interval)(refresh())(Threading.executionContext)
  } else {
   // info(l"Cannot publish ClockSignal value: not wired")
  }

  //To force a refresh in tests when clock is advanced
  def check(): Unit = {
    val lastRefresh = value.getOrElse(Instant.EPOCH)
    if (interval <= (now(clock).toEpochMilli - lastRefresh.toEpochMilli).millis) refresh()
  }

  override def onWire(): Unit = refresh()
}
