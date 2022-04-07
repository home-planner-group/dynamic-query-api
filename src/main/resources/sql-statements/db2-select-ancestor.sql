with recursive recursive_table (personName, birthYear, pNr, motherNr, fatherNr) as (
    -- anchor member
    select personName,
           birthYear,
           pNr,
           motherNr,
           fatherNr
    from ancestors
    where personName = 'Anne'
    union
    -- recursive member that references to the cte name
    select a.personName,
           a.birthYear,
           a.pNr,
           a.motherNr,
           a.fatherNr
    from ancestors a
             inner join recursive_table r
                        on r.motherNr = a.pNr
)
select *
from recursive_table;
