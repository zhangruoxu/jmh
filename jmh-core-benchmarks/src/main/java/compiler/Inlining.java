/*
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package compiler;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Benchmark)
public class Inlining {
    private ThreadLocalRandom random;
    private long acc;

    @Setup
    public void setup() {
        random = ThreadLocalRandom.current();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 1, jvmArgs = {
            "-XX:CompileCommand=dontinline,compiler/Inlining.get",
            "-XX:CompileCommand=dontinline,org/openjdk/jmh/infra/Blackhole.consume"
    })
    public void inlining(Blackhole bh) {
        long number = get();
        foo(number);
        bh.consume(acc);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 1, jvmArgs = {
            "-XX:CompileCommand=dontinline,compiler/Inlining.get",
            "-XX:CompileCommand=dontinline,compiler/Inlining.foo",
            "-XX:CompileCommand=dontinline,org/openjdk/jmh/infra/Blackhole.consume"
    })
    public void noInlining(Blackhole bh) {
        long number = get();
        foo(number);
        bh.consume(acc);
    }

    private long get() {
        return random.nextLong(0, 5);
    }

    private void foo(long number) {
        acc += number;
    }
}
