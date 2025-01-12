import React from 'react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from './ui/select';
import { Input } from './ui/input';
import { Button } from './ui/button';
import { Search, X } from 'lucide-react';

const STATUS_OPTIONS = [
  { value: 'DRAFT', label: '下書き' },
  { value: 'PENDING_APPROVAL', label: '承認待ち' },
  { value: 'IN_REVIEW', label: 'レビュー中' },
  { value: 'REJECTED', label: '差し戻し' },
  { value: 'IN_PROGRESS', label: '処理中' },
  { value: 'ON_HOLD', label: '保留中' },
  { value: 'COMPLETED', label: '完了' },
  { value: 'CANCELLED', label: '取消' }
];

export function WorkflowFilters({ filters, onFilterChange, onReset }) {
  const handleChange = (field, value) => {
    onFilterChange({ ...filters, [field]: value });
  };

  return (
    <div className="space-y-4">
      <div className="flex flex-wrap gap-4">
        <div className="w-64">
          <Select
            value={filters.status || ''}
            onValueChange={(value) => handleChange('status', value)}
          >
            <SelectTrigger>
              <SelectValue placeholder="ステータスで絞り込み" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="">すべてのステータス</SelectItem>
              {STATUS_OPTIONS.map(option => (
                <SelectItem key={option.value} value={option.value}>
                  {option.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        <div className="w-64">
          <Select
            value={filters.assigneeId || ''}
            onValueChange={(value) => handleChange('assigneeId', value)}
          >
            <SelectTrigger>
              <SelectValue placeholder="担当者で絞り込み" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="">すべての担当者</SelectItem>
              {filters.employees?.map(employee => (
                <SelectItem key={employee.id} value={employee.id.toString()}>
                  {employee.name} ({employee.department})
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        <div className="w-64">
          <Input
            type="date"
            value={filters.startDate || ''}
            onChange={(e) => handleChange('startDate', e.target.value)}
            placeholder="開始日"
          />
        </div>

        <div className="w-64">
          <Input
            type="date"
            value={filters.endDate || ''}
            onChange={(e) => handleChange('endDate', e.target.value)}
            placeholder="終了日"
          />
        </div>

        <div className="flex gap-2">
          <Button
            variant="outline"
            size="icon"
            onClick={onReset}
            title="フィルターをリセット"
          >
            <X className="h-4 w-4" />
          </Button>
          <Button
            variant="default"
            size="icon"
            onClick={() => onFilterChange(filters)}
            title="検索"
          >
            <Search className="h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  );
}