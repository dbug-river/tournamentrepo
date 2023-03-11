CREATE VIEW default.VW_UserRankings
(

    `game_id` Int64,

    `game_name` String,

    `provider` String,

    `user_id` String,

    `last_round` DateTime,

    `rounds` UInt64,

    `bet_amount` Decimal(38,
 2),

    `round_ranking` UInt64,

    `bet_amount_ranking` UInt64
) AS
SELECT
    dft.game_id AS game_id,

    dgd.game_name AS game_name,

    dgd.provider AS provider,

    dft.user_id AS user_id,

    max(dtd.timestamp_value) AS last_round,

    count(dft.user_id) AS rounds,

    sum(dft.real_amount_bet + dft.bonus_amount_bet) AS bet_amount,

    row_number() OVER (PARTITION BY dft.game_id ORDER BY rounds DESC,
 last_round DESC) AS round_ranking,

    row_number() OVER (PARTITION BY dft.game_id ORDER BY bet_amount DESC,
 last_round DESC) AS bet_amount_ranking
FROM default.DW_FactTable AS dft
INNER JOIN default.DW_TimeDimension AS dtd ON dft.created_timestamp_id = dtd.id
INNER JOIN default.DW_GameDimension AS dgd ON dft.game_id = dgd.game_id
GROUP BY
    dft.game_id,

    dgd.game_name,

    dgd.provider,

    dft.user_id;
