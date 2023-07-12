package blackjack.domain

import blackjack.domain.enums.Condition
import blackjack.domain.enums.MatchResult
import blackjack.dto.BlackjackGameMoneyResult

class BlackjackGame(
    val players: List<Player>,
    val dealer: Dealer
) {

    fun checkAllPlayersBlackjack(): Boolean {
        var blackjackFlag = false
        val blackjackPlayers = players.filter { it.condition == Condition.BLACKJACK }

        if (blackjackPlayers.isNotEmpty()) {
            players.filter { it.condition != Condition.BLACKJACK }.forEach { player -> player.loseAllBets() }
            if (dealer.condition != Condition.BLACKJACK) {
                blackjackPlayers.forEach { player -> player.blackjack() }
            }
            blackjackFlag = true
        }
        return blackjackFlag
    }

    fun resultBlackjackGameMoney(): List<BlackjackGameMoneyResult> {
        val result = mutableListOf<BlackjackGameMoneyResult>()

        players.forEach { player ->
            when (dealer.determineResult(player)) {
                MatchResult.WIN -> result.add(BlackjackGameMoneyResult(player.name, -player.betAmount))
                MatchResult.LOSE -> result.add(BlackjackGameMoneyResult(player.name, player.betAmount))
                MatchResult.DRAW -> result.add(BlackjackGameMoneyResult(player.name, 0.0))
            }
        }
        return result
    }
}
