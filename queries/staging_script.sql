-- Create staging table
CREATE TABLE default.Staging_GameRound
(
    created_timestamp DateTime,
    game_instance_id Int64,
    user_id VARCHAR(100),
    game_id Int64,
    real_amount_bet Nullable(Decimal(10, 2)),
    bonus_amount_bet Nullable(Decimal(10, 2)),
    real_amount_win Nullable(Decimal(10, 2)),
    bonus_amount_win Nullable(Decimal(10, 2)),
    game_name VARCHAR(100),
    provider VARCHAR(100),
    dw_status Int8
)
ENGINE = AggregatingMergeTree()
ORDER BY (created_timestamp, game_instance_id, user_id, game_id);