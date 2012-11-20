start transaction;
update analysis_calculation set calculation_string = 'notnull([Todo ID], equalto([Todo Completed], \"Completed\", 0, greaterthan([Todo Due At], 0, greaterthanorequal([Todo Due At], now(), 0, 1), 0)))' where calculation_string = 'equalto([Todo Completed], \"Completed\", 0, greaterthan([Todo Due At], 0, greaterthan([Todo Due At], now(), 0, 1), 0))';
commit;