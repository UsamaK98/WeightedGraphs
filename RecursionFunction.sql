CREATE OR REPLACE FUNCTION RECURSION(UPPER_P VARCHAR) RETURNS VOID AS $$
DECLARE 
    LOWER_P VARCHAR;
BEGIN
    -- print the current part number
    RAISE NOTICE 'Part number: %', UPPER_P;
    
    -- get the subparts for the current part number
    EXECUTE 'SELECT MINOR_P FROM PART_STRUCTURE WHERE MAJOR_P = $1 AND MINOR_P > $2 ORDER BY MINOR_P'
        INTO LOWER_P
        USING UPPER_P, ' ';
        
    -- iterate over the subparts using a depth-first search
    WHILE LOWER_P IS NOT NULL LOOP
        PERFORM RECURSION(LOWER_P);
        
        -- get the next subpart
        EXECUTE 'SELECT MINOR_P FROM PART_STRUCTURE WHERE MAJOR_P = $1 AND MINOR_P > $2 ORDER BY MINOR_P'
            INTO LOWER_P
            USING UPPER_P, LOWER_P;
    END LOOP;
END;
$$ LANGUAGE plpgsql;
