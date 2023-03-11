-- Create user dimension table to store user data.
CREATE TABLE default.DW_UserDimension
(
    user_id VARCHAR(100)
)
ENGINE = AggregatingMergeTree()
PRIMARY KEY (user_id)
ORDER BY (user_id);

-- Create time dimension table.
CREATE TABLE default.DW_TimeDimension
(
    id UUID,
    timestamp_value DateTime,
    day UInt16,
    month UInt16,
    year UInt16,
    hour UInt16
)
ENGINE = AggregatingMergeTree()
ORDER BY ( timestamp_value, day, month, year, hour);

-- Create game dimension table.
CREATE TABLE default.DW_GameDimension
(
    game_id Int64,
    game_name VARCHAR(100),
    provider VARCHAR(100)
)
ENGINE = AggregatingMergeTree()
PRIMARY KEY (game_id)
ORDER BY (game_id);

-- Create fact tabel to store bet and winning amount.
CREATE TABLE default.DW_FactTable
(
    created_timestamp_id UUID,
    game_instance_id Int64,
    user_id VARCHAR(100),
    game_id Int64,
    real_amount_bet Decimal(10, 2),
    bonus_amount_bet Decimal(10, 2),
    real_amount_win Decimal(10, 2),
    bonus_amount_win Decimal(10, 2)
)
ENGINE = AggregatingMergeTree()
PRIMARY KEY (created_timestamp_id, game_instance_id, user_id, game_id)
ORDER BY (created_timestamp_id, game_instance_id, user_id, game_id);
