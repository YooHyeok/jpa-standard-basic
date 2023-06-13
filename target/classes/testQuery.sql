 select * from member m
join team t on m.team_id = t.team_id;

select * from team t
join member m on t.TEAM_ID = m.TEAM_ID;