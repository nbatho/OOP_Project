package main.java.service.impl;

import java.util.List;
import java.util.UUID;
import main.java.model.Team;
import main.java.repository.TeamRepository;
import main.java.service.TeamService;

public class TeamServiceImpl implements TeamService {
    
    private final TeamRepository teamRepository;
    
    public TeamServiceImpl() {
        this.teamRepository = new TeamRepository();
    }
    
    @Override
    public boolean createTeam(Team team) {
        if (team == null) {
            System.err.println("Team object không được null");
            return false;
        }
        
        // Validate dữ liệu
        if (team.getName() == null || team.getName().trim().isEmpty()) {
            System.err.println("Tên team không được để trống");
            return false;
        }
        
        // Kiểm tra tên team đã tồn tại chưa
        if (isTeamNameExists(team.getName())) {
            System.err.println("Tên team đã tồn tại: " + team.getName());
            return false;
        }
        
        // Tự động tạo ID nếu chưa có
        if (team.getTeamId() == null || team.getTeamId().trim().isEmpty()) {
            team.setTeamId(UUID.randomUUID().toString());
        }
        
        return teamRepository.createTeam(team);
    }
    
    @Override
    public List<Team> getAllTeams() {
        return teamRepository.getAllTeams();
    }
    
    @Override
    public Team getTeamById(String teamId) {
        if (teamId == null || teamId.trim().isEmpty()) {
            System.err.println("Team ID không được để trống");
            return null;
        }
        return teamRepository.getTeamById(teamId);
    }
    
    @Override
    public Team getTeamByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Tên team không được để trống");
            return null;
        }
        return teamRepository.getTeamByName(name);
    }
    
    @Override
    public boolean updateTeam(Team team) {
        if (team == null || team.getTeamId() == null) {
            System.err.println("Team hoặc Team ID không được null");
            return false;
        }
        
        // Kiểm tra team có tồn tại không
        Team existingTeam = getTeamById(team.getTeamId());
        if (existingTeam == null) {
            System.err.println("Team không tồn tại với ID: " + team.getTeamId());
            return false;
        }
        
        // Nếu tên thay đổi, kiểm tra tên mới có trùng không
        if (!existingTeam.getName().equals(team.getName()) && isTeamNameExists(team.getName())) {
            System.err.println("Tên team mới đã tồn tại: " + team.getName());
            return false;
        }
        
        return teamRepository.updateTeam(team);
    }
    
    @Override
    public boolean updateTeamName(String teamId, String name) {
        if (teamId == null || teamId.trim().isEmpty()) {
            System.err.println("Team ID không được để trống");
            return false;
        }
        
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Tên team không được để trống");
            return false;
        }
        
        // Kiểm tra team có tồn tại không
        Team existingTeam = getTeamById(teamId);
        if (existingTeam == null) {
            System.err.println("Team không tồn tại với ID: " + teamId);
            return false;
        }
        
        // Kiểm tra tên mới có trùng không
        if (isTeamNameExists(name)) {
            System.err.println("Tên team đã tồn tại: " + name);
            return false;
        }
        
        // Cập nhật tên team
        existingTeam.setName(name);
        return teamRepository.updateTeam(existingTeam);
    }
    
    @Override
    public boolean deleteTeam(String teamId) {
        if (teamId == null || teamId.trim().isEmpty()) {
            System.err.println("Team ID không được để trống");
            return false;
        }
        
        // Kiểm tra team có tồn tại không
        Team existingTeam = getTeamById(teamId);
        if (existingTeam == null) {
            System.err.println("Team không tồn tại với ID: " + teamId);
            return false;
        }
        
        // TODO: Kiểm tra team có đang được sử dụng trong project nào không
        // Có thể thêm logic để không cho xóa team đang có project
        
        return teamRepository.deleteTeam(teamId);
    }
    
    @Override
    public boolean isTeamNameExists(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return getTeamByName(name) != null;
    }

    @Override
    public boolean teamExists(String teamId) {
        if (teamId == null || teamId.trim().isEmpty()) {
            return false;
        }
        Team team = getTeamById(teamId);
        return team != null;
    }
}
