package com.github.javinator9889.threading.threads.notifyingthread;

/*
 * Copyright © 2018 - present | ThreadingTools by Javinator9889
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 *
 * Created by Javinator9889 on 17/11/2018 - ThreadingTools.
 */

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OnThreadCompleteListener {
    void onThreadCompleteListener(@NotNull final Runnable thread, @Nullable Throwable exception);
}
