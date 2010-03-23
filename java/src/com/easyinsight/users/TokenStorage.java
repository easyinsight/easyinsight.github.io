package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import java.sql.*;

import org.jetbrains.annotations.Nullable;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 9:12:26 AM
 */
public class TokenStorage {
    public static final int GOOGLE_DOCS_TOKEN = 1;
    public static final int GOOGLE_ANALYTICS_TOKEN = 13;
    public static final int BASECAMP_TOKEN = 18;
    public static final int HIGHRISE_TOKEN = 23;

    public void saveToken(Token token, long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT token_id FROM TOKEN where user_id = ? AND token_type = ? AND data_source_id = ?");
            queryStmt.setLong(1, token.getUserID());
            queryStmt.setInt(2, token.getTokenType());
            queryStmt.setLong(3, dataSourceID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE TOKEN set token_value = ? WHERE token_id = ? AND data_source_id = ?");
                updateStmt.setString(1, token.getTokenValue());
                updateStmt.setLong(2, rs.getLong(1));
                if (dataSourceID == 0) {
                    updateStmt.setNull(3, Types.BIGINT);
                } else {
                    updateStmt.setLong(3, dataSourceID);
                }
                updateStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO TOKEN (token_type, user_id, token_value, data_source_id) VALUES (?, ?, ?, ?)");
                insertStmt.setInt(1, token.getTokenType());
                insertStmt.setLong(2, token.getUserID());
                insertStmt.setString(3, token.getTokenValue());
                if (dataSourceID == 0) {
                    insertStmt.setNull(4, Types.BIGINT);
                } else {
                    insertStmt.setLong(4, dataSourceID);
                }
                insertStmt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void saveToken(Token token, long dataSourceID, EIConnection conn) {

        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT token_id FROM TOKEN where user_id = ? AND token_type = ? AND data_source_id = ?");
            queryStmt.setLong(1, token.getUserID());
            queryStmt.setInt(2, token.getTokenType());
            queryStmt.setLong(3, dataSourceID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE TOKEN set token_value = ? WHERE token_id = ? AND data_source_id = ?");
                updateStmt.setString(1, token.getTokenValue());
                updateStmt.setLong(2, rs.getLong(1));
                if (dataSourceID == 0) {
                    updateStmt.setNull(3, Types.BIGINT);
                } else {
                    updateStmt.setLong(3, dataSourceID);
                }
                updateStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO TOKEN (token_type, user_id, token_value, data_source_id) VALUES (?, ?, ?, ?)");
                insertStmt.setInt(1, token.getTokenType());
                insertStmt.setLong(2, token.getUserID());
                insertStmt.setString(3, token.getTokenValue());
                if (dataSourceID == 0) {
                    insertStmt.setNull(4, Types.BIGINT);
                } else {
                    insertStmt.setLong(4, dataSourceID);
                }
                insertStmt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteToken(Token token) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TOKEN WHERE TOKEN_TYPE = ? AND USER_ID = ?");
            deleteStmt.setInt(1, token.getTokenType());
            deleteStmt.setLong(2, token.getUserID());
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    @Nullable
    public Token getToken(long userID, int type) {
        Token token = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT token_value FROM TOKEN where user_id = ? AND token_type = ?");
            queryStmt.setLong(1, userID);
            queryStmt.setInt(2, type);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                String tokenValue = rs.getString(1);
                token = new Token();
                token.setUserID(userID);
                token.setTokenType(type);
                token.setTokenValue(tokenValue);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return token;
    }

    public Token getToken(long userID, int type, long dataSourceID, boolean queryFromType) {
        Token token = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement dataSourceTokenStmt = conn.prepareStatement("SELECT token_value FROM TOKEN where data_source_id = ?");
            dataSourceTokenStmt.setLong(1, dataSourceID);
            ResultSet dsRS = dataSourceTokenStmt.executeQuery();
            if (dsRS.next()) {
                token = new Token();
                token.setTokenType(type);
                token.setTokenValue(dsRS.getString(1));
            } else {
                if (queryFromType) {
                    PreparedStatement queryStmt = conn.prepareStatement("SELECT token_value FROM TOKEN where user_id = ? AND token_type = ?");
                    queryStmt.setLong(1, userID);
                    queryStmt.setInt(2, type);
                    ResultSet rs = queryStmt.executeQuery();
                    if (rs.next()) {
                        String tokenValue = rs.getString(1);
                        token = new Token();
                        token.setUserID(userID);
                        token.setTokenType(type);
                        token.setTokenValue(tokenValue);
                        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO TOKEN (TOKEN_VALUE, TOKEN_TYPE, DATA_SOURCE_ID) VALUES (?, ?, ?)");
                        insertStmt.setString(1, tokenValue);
                        insertStmt.setInt(2, type);
                        insertStmt.setLong(3, dataSourceID);
                        insertStmt.execute();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return token;
    }

    public Token getToken(long userID, int type, long dataSourceID, boolean queryFromType, EIConnection conn) {
        Token token = null;

        try {
            PreparedStatement dataSourceTokenStmt = conn.prepareStatement("SELECT token_value FROM TOKEN where data_source_id = ?");
            dataSourceTokenStmt.setLong(1, dataSourceID);
            ResultSet dsRS = dataSourceTokenStmt.executeQuery();
            if (dsRS.next()) {
                token = new Token();
                token.setTokenType(type);
                token.setTokenValue(dsRS.getString(1));
            } else {
                if (queryFromType) {
                    PreparedStatement queryStmt = conn.prepareStatement("SELECT token_value FROM TOKEN where user_id = ? AND token_type = ?");
                    queryStmt.setLong(1, userID);
                    queryStmt.setInt(2, type);
                    ResultSet rs = queryStmt.executeQuery();
                    if (rs.next()) {
                        String tokenValue = rs.getString(1);
                        token = new Token();
                        token.setUserID(userID);
                        token.setTokenType(type);
                        token.setTokenValue(tokenValue);
                        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO TOKEN (TOKEN_VALUE, TOKEN_TYPE, DATA_SOURCE_ID) VALUES (?, ?, ?)");
                        insertStmt.setString(1, tokenValue);
                        insertStmt.setInt(2, type);
                        insertStmt.setLong(3, dataSourceID);
                        insertStmt.execute();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return token;
    }

    @Nullable
    public Token getToken(long userID, int type, Connection conn) {
        Token token = null;

        try {

            PreparedStatement queryStmt = conn.prepareStatement("SELECT token_value FROM TOKEN where user_id = ? AND token_type = ?");
            queryStmt.setLong(1, userID);
            queryStmt.setInt(2, type);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                String tokenValue = rs.getString(1);
                token = new Token();
                token.setUserID(userID);
                token.setTokenType(type);
                token.setTokenValue(tokenValue);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } 
        return token;
    }
}
