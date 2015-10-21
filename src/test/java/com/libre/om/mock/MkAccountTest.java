/**
 * Copyright (c) 2015, nerodesk.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the nerodesk.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.nerodesk.om.mock;

import com.nerodesk.om.Account;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for {@link MkAccount}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @since 0.4
 */
public final class MkAccountTest {
    /**
     * MkAccount starting balance is zero.
     */
    @Test
    public void startingBalanceIsZero() {
        final Account account = new MkAccount();
        MatcherAssert.assertThat(
            account.balance(), Matchers.equalTo(0)
        );
    }

    /**
     * MkAccounts should have no transactions at start.
     */
    @Test
    public void hasNoTransactionsAtStart() {
        final Account account = new MkAccount();
        MatcherAssert.assertThat(
            account.transactions(), Matchers.emptyIterable()
        );
    }

    /**
     * MkAccounts can add operation.
     */
    @Test
    public void addsOperation() {
        final Account account = new MkAccount();
        final String funding = "funding";
        final int amount = 1;
        account.add(amount, funding);
        MatcherAssert.assertThat(
            account.balance(),
            Matchers.equalTo(amount)
        );
        MatcherAssert.assertThat(
            account.transactions(),
            Matchers.contains(funding)
        );
    }

    /**
     * MkAccounts can return operations starting from the last.
     */
    @Test
    public void returnsOperationsStartingFromLast() {
        final Account account = new MkAccount();
        final String first = "first";
        final String second = "second";
        final int amount = 1;
        account.add(amount, first);
        account.add(amount, second);
        MatcherAssert.assertThat(
            account.balance(),
            Matchers.equalTo(2 * amount)
        );
        MatcherAssert.assertThat(
            account.transactions(),
            Matchers.contains(second, first)
        );
    }
}
