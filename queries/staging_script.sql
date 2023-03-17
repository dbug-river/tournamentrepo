-- Create staging table
CREATE TABLE default.Staging_GameRound
(
    created_timestamp DateTime,
    game_instance_id Int64,
    user_id VARCHAR(100), -- you can use string in clickhouse
    game_id Int64, -- too big , why?
    real_amount_bet Nullable(Decimal(10, 2)),
    bonus_amount_bet Nullable(Decimal(10, 2)),
    real_amount_win Nullable(Decimal(10, 2)),
    bonus_amount_win Nullable(Decimal(10, 2)),
    game_name VARCHAR(100),
    provider VARCHAR(100),
    dw_status Int8
)
ENGINE = AggregatingMergeTree() -- why are you using aggregate merge tree when you are not using aggregate functions?
ORDER BY (created_timestamp, game_instance_id, user_id, game_id);


/*You could have removed nulls in nifi and clickhouse table would be non nullable.
Why use 2 queries instead of one update nulls.


    ALTER TABLE Staging_GameRound_2
UPDATE bonus_amount_bet = coalesce(bonus_amount_bet, 0), real_amount_win = coalesce(real_amount_win, 0)
WHERE bonus_amount_bet is null
   or real_amount_win is null
  */
