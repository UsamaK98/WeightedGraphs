CREATE OR REPLACE FUNCTION find_path_and_weight(input text)
RETURNS TABLE (leaf_node text, path_weight numeric) AS $$
BEGIN
		RETURN QUERY
		WITH RECURSIVE cte AS (
		SELECT major_p, minor_p, qty, qty::numeric AS path_weight, 1 AS depth
		FROM part_structure
		WHERE major_p = input
		UNION ALL
		SELECT ps.major_p, ps.minor_p, ps.qty, cte.path_weight * ps.qty::numeric AS path_weight, cte.depth + 1 AS depth
		FROM part_structure AS ps
		JOIN cte ON ps.major_p = cte.minor_p
	)
	SELECT minor_p::text AS leaf_node, sum(cte.path_weight) AS path_weight
	FROM cte
	WHERE NOT EXISTS (SELECT 1 FROM part_structure WHERE major_p = cte.minor_p)
	GROUP BY minor_p;

END;
$$ LANGUAGE plpgsql;
