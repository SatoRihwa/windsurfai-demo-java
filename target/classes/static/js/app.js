// DOMが完全に読み込まれたら実行
document.addEventListener('DOMContentLoaded', function() {
    const taskForm = document.getElementById('taskForm');
    const taskList = document.getElementById('taskList');
    const cancelBtn = document.getElementById('cancelBtn');
    const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
    let tasks = [];
    let isEditing = false;
    let taskIdToDelete = null;

    // フォーム送信時の処理
    taskForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const taskData = {
            name: document.getElementById('taskName').value,
            description: document.getElementById('taskDescription').value,
            completed: false
        };

        try {
            if (isEditing) {
                const id = document.getElementById('taskId').value;
                await updateTask(id, taskData);
            } else {
                await createTask(taskData);
            }
            resetForm();
            await fetchTasks();
        } catch (error) {
            console.error('Error saving task:', error);
            alert('タスクの保存中にエラーが発生しました。');
        }
    });

    // キャンセルボタンの処理
    cancelBtn.addEventListener('click', function() {
        resetForm();
    });

    // 削除確認ボタンの処理
    document.getElementById('confirmDelete').addEventListener('click', async function() {
        if (taskIdToDelete) {
            try {
                await deleteTask(taskIdToDelete);
                deleteModal.hide();
                await fetchTasks();
            } catch (error) {
                console.error('Error deleting task:', error);
                alert('タスクの削除中にエラーが発生しました。');
            }
        }
    });

    // タスク一覧の取得
    async function fetchTasks() {
        try {
            const response = await fetch('/api/tasks');
            tasks = await response.json();
            renderTasks();
        } catch (error) {
            console.error('Error fetching tasks:', error);
        }
    }

    // タスクの作成
    async function createTask(taskData) {
        const response = await fetch('/api/tasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(taskData)
        });
        return await response.json();
    }

    // タスクの更新
    async function updateTask(id, taskData) {
        const response = await fetch(`/api/tasks/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(taskData)
        });
        return await response.json();
    }

    // タスクの削除
    async function deleteTask(id) {
        await fetch(`/api/tasks/${id}`, {
            method: 'DELETE'
        });
    }


    // タスクの完了/未完了を切り替え
    async function toggleTaskCompletion(id) {
        try {
            const response = await fetch(`/api/tasks/${id}/toggle`, {
                method: 'PATCH'
            });
            return await response.json();
        } catch (error) {
            console.error('Error toggling task completion:', error);
        }
    }


    // タスク一覧の表示
    function renderTasks() {
        taskList.innerHTML = '';
        
        if (tasks.length === 0) {
            taskList.innerHTML = '<div class="text-center py-3 text-muted">タスクがありません。新しいタスクを追加してください。</div>';
            return;
        }
        
        tasks.forEach(task => {
            const taskElement = document.createElement('div');
            taskElement.className = `card task-card mb-2 ${task.completed ? 'completed' : ''}`;
            taskElement.innerHTML = `
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start">
                        <div class="flex-grow-1">
                            <h6 class="task-title">${escapeHtml(task.name)}</h6>
                            ${task.description ? `<p class="task-description mb-0">${escapeHtml(task.description)}</p>` : ''}
                        </div>
                        <div class="task-actions ms-2 d-flex">
                            <div class="form-check form-switch me-2">
                                <input class="form-check-input toggle-complete" type="checkbox" 
                                    data-id="${task.id}" ${task.completed ? 'checked' : ''}>
                            </div>
                            <button class="btn btn-sm btn-outline-primary me-1 edit-btn" data-id="${task.id}">
                                <i class="bi bi-pencil">編集</i>
                            </button>
                            <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${task.id}" data-bs-toggle="modal" data-bs-target="#deleteModal">
                                <i class="bi bi-trash">削除</i>
                            </button>
                        </div>
                    </div>
                </div>
            `;
            
            taskList.appendChild(taskElement);
            
            // 編集ボタンのイベントリスナーを追加
            const editBtn = taskElement.querySelector('.edit-btn');
            editBtn.addEventListener('click', () => editTask(task));
            
            // 削除ボタンのイベントリスナーを追加
            const deleteBtn = taskElement.querySelector('.delete-btn');
            deleteBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                taskIdToDelete = task.id;
            });
            
            // 完了/未完了のトグルスイッチのイベントリスナーを追加
            const toggleSwitch = taskElement.querySelector('.toggle-complete');
            toggleSwitch.addEventListener('change', async (e) => {
                e.stopPropagation();
                const taskElement = e.target.closest('.task-card');
                taskElement.classList.toggle('completed');
                await toggleTaskCompletion(task.id);
                await fetchTasks();
            });
        });
    }

    // タスクの編集
    function editTask(task) {
        isEditing = true;
        document.getElementById('taskId').value = task.id;
        document.getElementById('taskName').value = task.name;
        document.getElementById('taskDescription').value = task.description || '';
        document.getElementById('saveBtn').textContent = '更新';
        cancelBtn.style.display = 'block';
        
        // スクロールしてフォームを表示
        taskForm.scrollIntoView({ behavior: 'smooth' });
        document.getElementById('taskName').focus();
    }

    // フォームをリセット
    function resetForm() {
        taskForm.reset();
        document.getElementById('taskId').value = '';
        document.getElementById('saveBtn').textContent = 'タスクを追加';
        cancelBtn.style.display = 'none';
        isEditing = false;
    }

    // HTMLエスケープ用のヘルパー関数
    function escapeHtml(unsafe) {
        if (!unsafe) return '';
        return unsafe
            .toString()
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }

    // 初期化
    fetchTasks();
});
