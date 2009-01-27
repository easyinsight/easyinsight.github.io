package com.easyinsight;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 2:12:27 PM
 */
public class DataAggregator {
    // <dataProvider id="">
    //  <dataFeed id="">
    //      <csv>file name</csv>
    //  </dataFeed>
    //  <dataFeed id="">
    //      <aggregate join="">
    //          <csv>file name</csv>
    //          <feed>id</feed>
    //      </aggregate>
    //  </dataFeed>
    // </dataProvider>

    // now, idea is that we'll eventually have thousands of data providers (and potentially many data feeds)
    // you want to have a mechanism to easily add in data providers
    // dialog of "find data provider"
    // within that, we want to provide the following...
    // general "search" on data providers, keyword
    // most popular data providers (broken out by categories)
    // add a data provider under a grouping, exposes the tree from there which lets you choose particular data feeds
    // should be able to group data feeds in folders underneath data providers
    // sports stats
    //     nba
    //     nfl
    //     mlb
    // etc
    // we provide a free connection to com.easyinsight.google spreadsaheets, amazon db, etc
    // export to csv, export to excel, export to com.easyinsight.google spreadsheet

    // there's an alternative way to join as well
    // which is, uh
    // <aggregate join="">
    //      <feed><transform></transform></feed>
    //      <feed>id</feed>

    // <dataProvider id="">
    //  <dataFeed id="">
    //      <selenium>
    //          <start>http://sports.espn.go.com/nba/statistics?stat=nbascoring&league=nba&sort=pts&season=2008</start>
    //          <table foundAt="0/0/0">
    //              <headers foundAt="">
    //                  <column label="NAME">
    //                      <split character=",">
    //                          <value label="Name">
    //                              <digger id="nbaPlayerDigger"/>
    //                          </value>
    //                          <value label="Team"></value>
    //                      </split>
    //                  </column>
    //                  <column label="GP">
    //                      <value>Games Played</value>
    //                  </column>
    //                  <column label="PTS"/>
    //              </headers>
    //              <columns foundAt="">
    //      </selenium>

    // <digger id="nbaPlayerDigger">
    //  <keyedBy>name path</keyedBy>
    //  <value label="Age">value path</value>
    //  <value label="Position">value path</value>
    //  <value label="Salary">value path</value>

    // write out as csv? maybe

    // policies are data provider centric
    // associate a charge to the data provider
    // access to a data provider will cost N dollars/T
    // question is how you define the data provider for custom scenarios
    // we just take a % of the charge on the provider
}
