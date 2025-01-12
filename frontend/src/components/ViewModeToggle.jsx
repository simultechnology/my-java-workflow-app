import React from 'react';
import { Button } from './ui/button';
import { List, LayoutGrid } from 'lucide-react';

export function ViewModeToggle({ mode, onModeChange }) {
  return (
    <div className="flex gap-2 p-2 bg-gray-100 rounded-md">
      <Button
        variant={mode === 'list' ? 'default' : 'ghost'}
        size="sm"
        onClick={() => onModeChange('list')}
        className="flex items-center gap-2"
      >
        <List className="h-4 w-4" />
        リスト表示
      </Button>
      <Button
        variant={mode === 'grid' ? 'default' : 'ghost'}
        size="sm"
        onClick={() => onModeChange('grid')}
        className="flex items-center gap-2"
      >
        <LayoutGrid className="h-4 w-4" />
        グリッド表示
      </Button>
    </div>
  );
}